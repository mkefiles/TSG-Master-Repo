package net.yorksolutions;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// DESC: Tell Spring that this is a Configuration Bean
// NOTE: Configuration Beans are, typically, a source of
// ... additional @Bean definitions, each of which is
// ... individually registered as their own Bean
@Configuration
public class RabbitMQConfig {

    private final String queueName = "employer.hra-config.queue";
    private final String topicExchangeName = "gravie.employers.exchange";
    private final String bindingRoutingKey = "employer.hra-config.created";

    @Bean
    // DESC: Establish the apps Queue
    // NOTE: A Queue is the ordered collection of messages that is
    // ... published-to/consumed-from in a F.I.F.O. manner
    public Queue queue() {
        // DESC: Initialize a new Queue given Name and Durability
        // NOTE: Per documentation, 'durability' refers to whether
        // ... the Queue will survive a server restart (in this
        // ... situation, I think that is superfluous)
        return new Queue(queueName, false);
    }

    // DESC: Establish the apps Exchange
    // NOTE: A "Topic" Exchange, specfically, handles routing messages
    // ... to a specific location
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    // DESC: Establish the relationship between the Queue and
    // ... the Topic Exchange
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(bindingRoutingKey);
    }
}
