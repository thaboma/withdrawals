package za.co.sanlam.fintech.withdrawal.enums;

public enum Status {

	ACTIVE("Active account"),
	SUSPENDED("Suspended account"),
	NOTIFICATION_FAILURE("Failed to send SNS notification"),
	SUCCESS("Withdrawal successful"),
	FAILURE("Withdrawal failed"),
	INSUFFICIENT("Insufficient funds for withdrawal");

	String message;

	Status(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}