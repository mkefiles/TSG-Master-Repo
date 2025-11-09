package net.yorksolutions.publisher;

import net.yorksolutions.api.SuccessfulConfigurationEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class ConfigurationService {

    private final RabbitTemplate rabbitTemplate;

    public ConfigurationService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public ConfigurationMessageDTO convertConfigurationToMessage(SuccessfulConfigurationEvent event) {
        return new ConfigurationMessageDTO(
                UUID.randomUUID(), Instant.now(), event.getEmployerId(),
                event.getConfigurationDTO().getPlanYear(),
                event.getConfigurationDTO().getMonthlyAllowance()
        );
    }

    public void publishMessageToQueue(String message) {
        this.rabbitTemplate.convertAndSend(
                "gravie.employers.exchange", "employer.hra-config.created",
                message
        );
        System.out.println("Message was published!");
    }

    @EventListener
    public void retrieveNewConfigurationEvent(SuccessfulConfigurationEvent event) {
        ConfigurationMessageDTO convertedEvent = convertConfigurationToMessage(event);
        publishMessageToQueue(convertedEvent.toString());
    }
}
