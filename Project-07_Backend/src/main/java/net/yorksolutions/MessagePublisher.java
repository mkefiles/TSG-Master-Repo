package net.yorksolutions;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessagePublisher {
    private final RabbitTemplate rabbitTemplate;

    public MessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage() {
        this.rabbitTemplate.convertAndSend("subway", "Hello, RabbitMQ!");
        System.out.println("Message was sent!");
    }
}
