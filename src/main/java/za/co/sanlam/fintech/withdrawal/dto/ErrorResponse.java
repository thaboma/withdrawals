package za.co.sanlam.fintech.withdrawal.dto;

import lombok.Builder;

public record ErrorResponse(
		int code,
		String source,
		String details

) {
	@Builder
	public ErrorResponse {
	}
}
