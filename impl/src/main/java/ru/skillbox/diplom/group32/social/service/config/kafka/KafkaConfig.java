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
import ru.skillbox.diplom.group32.social.service.model.streaming.StreamingMessageDto;

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
                .name("sender-account-online")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public ConsumerFactory<String, StreamingMessageDto> consumerDialogFactory() {
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
                new JsonDeserializer<>(StreamingMessageDto.class));
    }

    @Bean
    public ConsumerFactory<String, AccountOnlineDto> consumerAccountOnlineFactory() {
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
    public ConcurrentKafkaListenerContainerFactory<String, StreamingMessageDto> dialogListener() {
        ConcurrentKafkaListenerContainerFactory<String, StreamingMessageDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerDialogFactory());

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountOnlineDto> accountOnlineListener() {
        ConcurrentKafkaListenerContainerFactory<String, AccountOnlineDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerAccountOnlineFactory());

        return factory;
    }


    @Bean
    public ProducerFactory<String, StreamingMessageDto> producerDialogFactory() {
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
    public ProducerFactory<String, AccountOnlineDto> producerAccountOnlineFactory() {
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
    public KafkaTemplate<String, StreamingMessageDto> kafkaDialogTemplate() {
        KafkaTemplate<String, StreamingMessageDto> kafkaTemplate = new KafkaTemplate<>(producerDialogFactory());
        kafkaTemplate.setConsumerFactory(consumerDialogFactory());
        return kafkaTemplate;
    }

    @Bean
    public KafkaTemplate<String, AccountOnlineDto> kafkaAccountOnlineTemplate() {
        KafkaTemplate<String, AccountOnlineDto> kafkaTemplate = new KafkaTemplate<>(producerAccountOnlineFactory());
        kafkaTemplate.setConsumerFactory(consumerAccountOnlineFactory());
        return kafkaTemplate;
    }

}
