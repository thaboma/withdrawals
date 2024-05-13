package za.co.sanlam.fintech.withdrawal.dto;

import lombok.Builder;

import java.math.BigDecimal;

public record WithdrawalResponseDto(
		Long accountId,
		BigDecimal withdrawalAmount,
		BigDecimal balance,
		String status
) {
	@Builder
	public WithdrawalResponseDto {
	}
}

