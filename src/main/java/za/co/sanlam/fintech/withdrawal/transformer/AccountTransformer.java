package za.co.sanlam.fintech.withdrawal.transformer;

import za.co.sanlam.fintech.withdrawal.dto.WithdrawalResponseDto;
import za.co.sanlam.fintech.withdrawal.entity.AccountAuditEvent;
import za.co.sanlam.fintech.withdrawal.enums.Status;

import java.math.BigDecimal;
import java.util.Date;

public class AccountTransformer {

	public static AccountAuditEvent buildAuditEvent(Long accountId, WithdrawalResponseDto withdrawalResponseDto) {
		return AccountAuditEvent
				.builder()
				.accountId(accountId)
				.createddate(new Date())
				.status(withdrawalResponseDto.status())
				.payload(withdrawalResponseDto.toString())
				.build();
	}

	public static AccountAuditEvent buildAuditEvent(Long accountId, Status status, String payload) {
		return AccountAuditEvent
				.builder()
				.accountId(accountId)
				.createddate(new Date())
				.status(status.getMessage())
				.payload(payload)
				.build();
	}

	public static WithdrawalResponseDto buildWithdrawalResponseDto(Long accountId, BigDecimal amount, BigDecimal balance, Status status) {
		return WithdrawalResponseDto.builder().accountId(accountId).withdrawalAmount(amount).balance(balance).status(status.getMessage()).build();
	}

}
