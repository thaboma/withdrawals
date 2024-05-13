package za.co.sanlam.fintech.withdrawal.service;

import za.co.sanlam.fintech.withdrawal.dao.AccountAuditEventRepository;
import za.co.sanlam.fintech.withdrawal.dao.AccountRepository;
import za.co.sanlam.fintech.withdrawal.dto.WithdrawalRequestDto;
import za.co.sanlam.fintech.withdrawal.dto.WithdrawalResponseDto;
import za.co.sanlam.fintech.withdrawal.entity.Account;
import za.co.sanlam.fintech.withdrawal.enums.Status;

import java.util.Optional;

public interface AccountService {

	Optional<Account> findByAccountId(Long id);

	void updateAccount(Account account, WithdrawalResponseDto withdrawalResponseDto);

	void saveOrUpdateAccount(Account account);

	void saveAuditEvent(Long accountId, Status status, String payload);

	WithdrawalResponseDto withdraw(WithdrawalRequestDto withdrawalRequestDto);

	default AccountRepository getAccountRepository() {
		return null;
	}

	default AccountAuditEventRepository getAccounAccountAuditEventRepository() {
		return null;
	}

}
