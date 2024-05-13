package za.co.sanlam.fintech.withdrawal.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import za.co.sanlam.fintech.withdrawal.config.AppConfig;
import za.co.sanlam.fintech.withdrawal.dto.WithdrawalRequestDto;
import za.co.sanlam.fintech.withdrawal.dto.WithdrawalResponseDto;
import za.co.sanlam.fintech.withdrawal.entity.Account;
import za.co.sanlam.fintech.withdrawal.entity.Transaction;
import za.co.sanlam.fintech.withdrawal.enums.Status;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = { AppConfig.class })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountServiceTest {

    @Autowired
 	private AccountService accountService;

	private Collection<Account> accounts;

	@BeforeAll
	void setUp() {
		accounts=buildAccounts();
		accounts.stream().forEach(acc->accountService.saveOrUpdateAccount(acc));
	}

	@AfterAll
	void tearDown() {
		accountService.getAccountRepository().deleteAll();
		accountService.getAccounAccountAuditEventRepository().deleteAll();
	}

	@Test
	void findByAccountId() {
		var account1=accountService.findByAccountId(1L);
		assertNotNull(account1);
	}

	@Test
	void saveOrUpdateAccount() {
		accountService.saveOrUpdateAccount(buildAccount());
		var allDbAccounts =accountService.getAccountRepository().findAll();
		assertTrue(allDbAccounts.size()==3);
	}

	@Test
	void updateAccount() {

		var account1=accountService.findByAccountId(1L).get();
		var amount =new BigDecimal(4000);

		System.out.println("account1.getBalance()="+account1.getBalance()+"  ? ="+new BigDecimal(10000));
		assertEquals(0,account1.getBalance().compareTo(new BigDecimal(10000)));
		account1.setBalance(account1.getBalance().subtract(amount));

		var withdrawalResponseDto = WithdrawalResponseDto.builder().accountId(1L).withdrawalAmount(amount).balance(account1.getBalance()).status(Status.ACTIVE.getMessage()).build();
		accountService.updateAccount(account1,withdrawalResponseDto);

		var updateAccount1=accountService.findByAccountId(1L).get();
		assertEquals(0,updateAccount1.getBalance().compareTo(new BigDecimal(6000)));

		var auditEvents=accountService.getAccounAccountAuditEventRepository().findAll();
		var auditEvent=auditEvents.stream().findFirst().get();

		assertTrue(auditEvent.getAccountId().equals(withdrawalResponseDto.accountId()));
		assertEquals(0,amount.compareTo(withdrawalResponseDto.withdrawalAmount()));
		assertEquals(0,account1.getBalance().compareTo(withdrawalResponseDto.balance()));
		assertTrue(auditEvent.getAccountId().equals(1L));

	}

	@Test
	void saveAuditEvent() {
		accountService.saveAuditEvent(1L,Status.SUSPENDED,"Test saving audit event");
		var auditEvents=accountService.getAccounAccountAuditEventRepository().findAll();
		assertTrue(auditEvents.size()==1);
	}


	@Test
	void withdraw() {
		var amount =new BigDecimal(4000);
		var withdrawalRequestDto = WithdrawalRequestDto.builder().accountId(1L).amount(amount).build();
		var withdrawalResponseDto=accountService.withdraw(withdrawalRequestDto);
		assertEquals(0,withdrawalResponseDto.withdrawalAmount().compareTo(amount));

		var updatedAcc =accountService.findByAccountId(1L).get();
		assertEquals(0,updatedAcc.getBalance().compareTo(new BigDecimal(6000)));
		assertEquals(0,updatedAcc.getBalance().compareTo(withdrawalResponseDto.balance()));

		var auditEvents=accountService.getAccounAccountAuditEventRepository().findAll();
		assertTrue(auditEvents.size() >=1);// The second aduit events is due to the failed SNS notification call
	}

	private Collection<Account> buildAccounts(){

		var account1=Account.builder().id(1L).accountName("CHEQUE").status(Status.ACTIVE).balance(new BigDecimal(10000)).createdDate(new Date()).build();
		var account2=Account.builder().id(2L).accountName("SAVINGS").status(Status.ACTIVE).balance(new BigDecimal(5000)).createdDate(new Date()).build();

		var transaction1 = Transaction.builder().id(UUID.randomUUID()).account(account1).amount(new BigDecimal(10000)).type(Transaction.Type.WITHDRAWAL).accountId(account1.getId()).transactionDate(new Date()).build();
		account1.setTransactions(Set.of(transaction1));

		var transaction2 = Transaction.builder().id(UUID.randomUUID()).account(account2).amount(new BigDecimal(5000)).type(Transaction.Type.WITHDRAWAL).accountId(account2.getId()).transactionDate(new Date()).build();
		account2.setTransactions(Set.of(transaction2));

		 return List.of(account1,account2);
	}

	private Account buildAccount(){
		return Account.builder().id(3L).accountName("CHEQUE").status(Status.ACTIVE).balance(new BigDecimal(4000)).createdDate(new Date()).build();

	}

}