package ru.aston.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic userGetRequestTopic() {
        return new NewTopic("user-get-request", 1, (short) 1);
    }

    @Bean
    public NewTopic userGetResponseTopic() {
        return new NewTopic("user-get-response", 1, (short) 1);
    }

    @Bean
    public NewTopic userListGetRequestTopic() {
        return new NewTopic("users-list-get-request", 1, (short) 1);
    }

    @Bean
    public NewTopic userListGetResponseTopic() {
        return new NewTopic("users-list-get-response", 1, (short) 1);
    }
}