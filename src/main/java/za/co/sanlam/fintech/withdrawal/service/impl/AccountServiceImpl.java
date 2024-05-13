package za.co.sanlam.fintech.withdrawal.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import software.amazon.awssdk.core.exception.SdkClientException;
import za.co.sanlam.fintech.withdrawal.client.NotificationClient;
import za.co.sanlam.fintech.withdrawal.dao.AccountAuditEventRepository;
import za.co.sanlam.fintech.withdrawal.dao.AccountRepository;
import za.co.sanlam.fintech.withdrawal.dto.WithdrawalRequestDto;
import za.co.sanlam.fintech.withdrawal.dto.WithdrawalResponseDto;
import za.co.sanlam.fintech.withdrawal.entity.Account;
import za.co.sanlam.fintech.withdrawal.entity.Transaction;
import za.co.sanlam.fintech.withdrawal.enums.Status;
import za.co.sanlam.fintech.withdrawal.service.AccountService;
import za.co.sanlam.fintech.withdrawal.transformer.AccountTransformer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountAuditEventRepository accountAuditEventRepository;

	@Autowired
	private NotificationClient notificationClient;

	@Override
	public Optional<Account> findByAccountId(Long id) {
		return accountRepository.findById(id);
	}

	@Override
	@Transactional
	public void saveAuditEvent(Long accountId, Status status, String payload) {
		var auditEvent = AccountTransformer.buildAuditEvent(accountId, status, payload);
		accountAuditEventRepository.saveAndFlush(auditEvent);
	}

	@Override
	@Transactional
	public void updateAccount(Account account, WithdrawalResponseDto withdrawalResponseDto) {
		accountRepository.save(account);
		var auditEvent = AccountTransformer.buildAuditEvent(account.getId(), withdrawalResponseDto);
		accountAuditEventRepository.saveAndFlush(auditEvent);
	}

	@Transactional
	@Override public void saveOrUpdateAccount(Account account) {
		accountRepository.saveAndFlush(account);
	}

	@Override
	public WithdrawalResponseDto withdraw(WithdrawalRequestDto withdrawalRequestDto) {
		var accountId = withdrawalRequestDto.accountId();
		var amount = withdrawalRequestDto.amount();
		var account = findByAccountId(accountId).orElse(null);

		if (ObjectUtils.isEmpty(account)) {
			log.warn("Failed to make withdrawal on account {} as account does not exist", accountId);
			var response = AccountTransformer.buildWithdrawalResponseDto(accountId, amount, null, Status.FAILURE);
			this.saveAuditEvent(accountId, Status.FAILURE, String.format("Failed to make withdrawal on account [%s] as account does not exist", accountId));

			return response;
		}

		if (hasPositiveBalance(account, amount)) {
			return makeWithdrawal(account, amount);
		} else {
			log.warn("Failed to make withdrawal on account {} due to insufficient funds", account.getId());
			var response = AccountTransformer.buildWithdrawalResponseDto(accountId, amount, account.getBalance(), Status.INSUFFICIENT);
			this.updateAccount(account, response);

			return response;
		}
	}

	private boolean hasPositiveBalance(Account account, BigDecimal amount) {
		var currentBalance = account.getBalance().subtract(amount);
		return currentBalance != null && currentBalance.compareTo(amount) >= 0;
	}

	private WithdrawalResponseDto makeWithdrawal(Account account, BigDecimal amount) {
		var accountId = account.getId();

		try {
			var currentBalance = account.getBalance().subtract(amount);
			var response = AccountTransformer.buildWithdrawalResponseDto(accountId, amount, currentBalance, Status.SUCCESS);
			account.setBalance(currentBalance);
			var transaction =Transaction.builder().id(UUID.randomUUID()).account(account).amount(amount).type(Transaction.Type.WITHDRAWAL).accountId(accountId).transactionDate(new Date()).build();
			account.setTransactions(Set.of(transaction));

			this.updateAccount(account, response);
			notificationClient.sendWithdrawalNotification(response);
			log.info("Made successful withdrawal of {} withdrawalAmount on account {} ", amount, accountId);

			return response;
		} catch (SdkClientException snsEx) {

			log.error("Error when trying to make send withdrawal SNS notification on account {} : {}", account.getId(), snsEx);
			Status status = Status.NOTIFICATION_FAILURE;
			var response = AccountTransformer.buildWithdrawalResponseDto(accountId, amount, account.getBalance(), status);
			this.saveAuditEvent(accountId, status, StringUtils.left(String.format("Failed to send SNS notification after withdrawal for account [%s] due to %s", accountId, snsEx.getMessage()), 200));

			return response;
		} catch (Exception e) {

			log.error("ErrorResponse when trying to make withdrawal on account {} : {}", account.getId(), e);
			Status status = Status.FAILURE;
			var response = AccountTransformer.buildWithdrawalResponseDto(accountId, amount, account.getBalance(), status);
			this.saveAuditEvent(accountId, status, StringUtils.left(e.getMessage(), 200));
			this.saveAuditEvent(accountId, status, StringUtils.left(String.format("ErrorResponse when trying to make withdrawal on account [%s] due to %s", accountId, e.getMessage()), 200));

			return response;
		}
	}

	@Override
	public AccountRepository getAccountRepository() {
		return accountRepository;
	}

	@Override
	public AccountAuditEventRepository getAccounAccountAuditEventRepository() {
		return accountAuditEventRepository;
	}
}
