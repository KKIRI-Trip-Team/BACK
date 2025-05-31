package com.kkiri_trip.back.global.config.chat;

import com.kkiri_trip.back.api.dto.ChatMessage.ChatMessageDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import software.amazon.awssdk.utils.ImmutableMap;

import java.util.Map;

@EnableKafka
@Configuration
public class ListenerConfiguration {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ChatMessageDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ChatMessageDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // 폴링 타임아웃 설정
        factory.getContainerProperties().setPollTimeout(30000); // 폴링 타임아웃은 30초로 설정
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ChatMessageDto> consumerFactory() {
        JsonDeserializer<ChatMessageDto> deserializer = new JsonDeserializer<>(ChatMessageDto.class);
        deserializer.addTrustedPackages("*"); // 모든 패키지를 신뢰

        Map<String, Object> consumerConfigurations =
                ImmutableMap.<String, Object>builder()
                        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
                        .put(ConsumerConfig.GROUP_ID_CONFIG, "chat-group") // groupId 설정
                        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class)
                        .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest") // 처음부터 소비
                        // 세션 타임아웃을 Kafka 클러스터의 설정 범위 내로 설정 (5분)
                        .put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000") // 세션 타임아웃 30초
                        // 최대 폴링 시간과 대기 시간도 설정
                        .put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "300000") // 최대 폴링 시간 5분
                        .put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "30000") // 메시지 대기 시간 30초
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
    }
}
