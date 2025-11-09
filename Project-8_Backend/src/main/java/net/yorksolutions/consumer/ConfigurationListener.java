package net.yorksolutions.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationListener {

    @RabbitListener(queues = "employer.hra-config.queue")
    public void receiveMessage(String message) {
        System.out.println("OPS NOTIFICATION: " + message);
    }
}
