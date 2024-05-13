package za.co.sanlam.fintech.withdrawal.dto;

import lombok.Builder;

import java.math.BigDecimal;

public record WithdrawalRequestDto(
		Long accountId,
		BigDecimal amount
) {
	@Builder
	public WithdrawalRequestDto {
	}
}
