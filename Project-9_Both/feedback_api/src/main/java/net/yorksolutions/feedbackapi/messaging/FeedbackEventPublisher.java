package net.yorksolutions.feedbackapi.messaging;

import net.yorksolutions.feedbackapi.dtos.FeedbackResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class FeedbackEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${topic.name}")
    private String topicName;

    public FeedbackEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendFeedbackEvent(FeedbackResponse feedbackResponse) {

        FeedbackEvent event = new FeedbackEvent(
                feedbackResponse.getId(),
                feedbackResponse.getMemberId(),
                feedbackResponse.getProviderName(),
                feedbackResponse.getRating(),
                feedbackResponse.getComment(),
                feedbackResponse.getSubmittedAt(),
                "1.0.0"
        );

        kafkaTemplate.send(topicName, event.id().toString(), event);
        System.out.println("Sent JSON feedback event: " + event);
    }
}
