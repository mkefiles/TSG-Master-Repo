package net.yorksolutions;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    @RabbitListener(queues = "subway")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }

}
