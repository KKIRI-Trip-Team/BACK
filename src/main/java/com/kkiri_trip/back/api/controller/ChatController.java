package com.kkiri_trip.back.api.controller;


import com.kkiri_trip.back.api.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final KafkaTemplate<String, ChatMessage> kafkaTemplate;

    // 클라이언트가 /publish/chat 으로 보낸 메시지를 수신
    @MessageMapping("/chat")
    public void sendMessage(ChatMessage message) {
        System.out.println(">> Kafka 전송 시도: " + message);
        kafkaTemplate.send("chat-topic", String.valueOf(message.getPostId()), message);
    }

}