package za.co.sanlam.fintech.withdrawal.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import za.co.sanlam.fintech.withdrawal.dto.WithdrawalResponseDto;

@Slf4j
@Component
public class NotificationClient {

	@Value("${arn.aws.sns.reg.acc-id.topic-name:arn:aws:sns:US_WEST_2:ACCOUNT_ID:TOPIC_NAME}")
	private String snsTopicArn;

	private SnsClient snsClient;

	public NotificationClient() {

		this.snsClient = SnsClient.builder().region(Region.US_WEST_2).build();
	}

	public PublishResponse sendWithdrawalNotification(WithdrawalResponseDto withdrawalResponseDto) throws JsonProcessingException {
		String eventJson = (new ObjectMapper()).writeValueAsString(withdrawalResponseDto);
		PublishRequest publishRequest = PublishRequest.builder().message(eventJson).topicArn(snsTopicArn).build();

		return snsClient.publish(publishRequest);
	}

}
