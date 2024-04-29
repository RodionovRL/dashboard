package ru.aston.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for Kafka consumers.
 * This class sets up the configuration for Kafka consumers, including the bootstrap servers and deserializers.
 */
@Configuration
public class KafkaConsumerConfig {

    /**
     * The address of the Kafka bootstrap servers.
     */
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    /**
     * Creates a consumer factory for Kafka consumers.
     * @return ConsumerFactory for Kafka consumers.
     */
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    /**
     * Creates a ConcurrentKafkaListenerContainerFactory for Kafka consumers.
     * @return ConcurrentKafkaListenerContainerFactory for Kafka consumers.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}