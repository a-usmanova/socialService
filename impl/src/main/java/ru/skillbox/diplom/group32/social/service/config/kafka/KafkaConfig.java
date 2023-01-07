package ru.skillbox.diplom.group32.social.service.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.skillbox.diplom.group32.social.service.model.account.AccountOnlineDto;
import ru.skillbox.diplom.group32.social.service.model.dialog.DialogMessage;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Bean
    NewTopic dialogTopic() {
        return TopicBuilder
                .name("sender-message")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic notificationTopic() {
        return TopicBuilder
                .name("sender-notification")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public ConsumerFactory<String, DialogMessage> consumerDialogFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServer);
        config.put(ConsumerConfig.GROUP_ID_CONFIG,
                consumerGroupId);
        config.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );
        config.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class
        );

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
                new JsonDeserializer<>(DialogMessage.class));
    }

    @Bean
    public ConsumerFactory<String, AccountOnlineDto> consumerNotificationFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServer);
        config.put(ConsumerConfig.GROUP_ID_CONFIG,
                consumerGroupId);
        config.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );
        config.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class
        );

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
                new JsonDeserializer<>(AccountOnlineDto.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DialogMessage> dialogListener() {
        ConcurrentKafkaListenerContainerFactory<String, DialogMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerDialogFactory());

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountOnlineDto> notificationListener() {
        ConcurrentKafkaListenerContainerFactory<String, AccountOnlineDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerNotificationFactory());

        return factory;
    }


    @Bean
    public ProducerFactory<String, DialogMessage> producerDialogFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServer);
        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );
        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(),
                new JsonSerializer<>());
    }

    @Bean
    public ProducerFactory<String, AccountOnlineDto> producerNotificationFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServer);
        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );
        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(),
                new JsonSerializer<>());
    }

    @Bean
    public KafkaTemplate<String, DialogMessage> kafkaDialogTemplate() {
        KafkaTemplate<String, DialogMessage> kafkaTemplate = new KafkaTemplate<>(producerDialogFactory());
        kafkaTemplate.setConsumerFactory(consumerDialogFactory());
        return kafkaTemplate;
    }

    @Bean
    public KafkaTemplate<String, AccountOnlineDto> kafkaNotificationTemplate() {
        KafkaTemplate<String, AccountOnlineDto> kafkaTemplate = new KafkaTemplate<>(producerNotificationFactory());
        kafkaTemplate.setConsumerFactory(consumerNotificationFactory());
        return kafkaTemplate;
    }

}
