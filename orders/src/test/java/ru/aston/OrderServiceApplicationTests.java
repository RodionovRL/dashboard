package ru.aston;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import ru.aston.controller.privatecontroller.PrivateOrderController;
import ru.aston.dto.NewOrderDto;
import ru.aston.dto.OrderDto;
import ru.aston.kafka.config.KafkaConsumerConfig;
import ru.aston.kafka.config.KafkaProducerConfig;
import ru.aston.kafka.config.KafkaTopicConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderServiceApplication.class)
@ContextConfiguration(initializers = {OrderServiceApplicationTests.Initializer.class})
public class OrderServiceApplicationTests {

    @Autowired
    PrivateOrderController privateOrderController;

   @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("test")
            .withPassword("test");

    @ClassRule
    public static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.5.3"))
                    .withExposedPorts(9093);

    @Test
    public void successCreateOrderTest() {
        NewOrderDto newOrderDto = new NewOrderDto("Order1", "Order1 Desription");

        OrderDto orderDto = privateOrderController.postOrder(1L, newOrderDto);

        Assert.assertEquals(Long.valueOf(1), orderDto.getId());
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers(),
                    "spring.kafka.consumer.auto-offset-reset=earliest"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}