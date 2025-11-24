package net.yorksolutions.feedbackconsumer.messaging;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, FeedbackEvent> consumerFactory(KafkaProperties kafkaProperties) {

        JsonDeserializer<FeedbackEvent> deserializer =
                new JsonDeserializer<>(FeedbackEvent.class);

        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(false);

        Map<String, Object> props = kafkaProperties.buildConsumerProperties();

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FeedbackEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, FeedbackEvent> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, FeedbackEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
