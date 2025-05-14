package com.kkiri_trip.back.global.chat;

import com.kkiri_trip.back.api.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    // Kafka에서 메시지를 받으면
    @KafkaListener(topics = "chat-topic", groupId = "chat-group")
    public void consume(ChatMessage message) {
        // /subscribe/chat/{postId}로 다시 broadcast (구독자들에게)
        messagingTemplate.convertAndSend(
                "/subscribe/chat/" + message.getPostId(),
                message
        );
    }
}
