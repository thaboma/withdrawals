package za.co.sanlam.fintech.withdrawal;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import za.co.sanlam.fintech.withdrawal.entity.Account;
import za.co.sanlam.fintech.withdrawal.entity.Transaction;
import za.co.sanlam.fintech.withdrawal.enums.Status;
import za.co.sanlam.fintech.withdrawal.service.AccountService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
@Slf4j
public class WithdrawalApplication {

	@Autowired
	private AccountService accountService;

	public static void main(String[] args) {
		SpringApplication.run(WithdrawalApplication.class, args);
	}

	@PostConstruct
	void init() {
		var amount =new BigDecimal(100000);
		var account = Account.builder().id(1L).accountName("CHEQUE").createdDate(new Date()).balance(amount).status(Status.ACTIVE).build();
		var transaction = Transaction.builder().id(UUID.randomUUID()).account(account).amount(amount).type(Transaction.Type.DEPOSIT).accountId(account.getId()).transactionDate(new Date()).build();
		account.setTransactions(Set.of(transaction));
		accountService.saveOrUpdateAccount(account);
	}

}
