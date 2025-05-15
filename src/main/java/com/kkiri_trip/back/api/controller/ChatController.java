package com.kkiri_trip.back.api.controller;

import com.kkiri_trip.back.api.dto.ChatMessage.ChatMessageDto;
import com.kkiri_trip.back.domain.chat.ChatFileService;
import com.kkiri_trip.back.domain.chat.ChatMessageService;
import com.kkiri_trip.back.infra.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final KafkaTemplate<String, ChatMessageDto> kafkaTemplate;
    private final ChatFileService chatFileService;
    private final S3Service s3Service;

    @MessageMapping("/chat")
    public void sendMessage(@RequestBody ChatMessageDto message) {
        // 메시지에 이미지 URL이 포함되어 있으면
        if (message.getMessageType().equals("IMAGE")) {
            // 업로드된 이미지 URL이 포함되어 있다면 추가적인 처리
            String imageUrl = message.getImageUrl();
            String thumbnailUrl = message.getThumbnailUrl();

            // 이곳에서 이미지 URL 처리와 저장 작업을 수행
            message.setContent(thumbnailUrl);

            // 메시지 저장 및 Kafka 전송
            chatMessageService.saveMessage(message);
            kafkaTemplate.send("chat-topic", String.valueOf(message.getChatRoomId()), message);
        } else {
            // 텍스트 메시지나 이모지 등의 처리
            chatMessageService.saveMessage(message);
            kafkaTemplate.send("chat-topic", String.valueOf(message.getChatRoomId()), message);
        }
    }


    @PostMapping("/chat/uploadImage")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String imageUrl = s3Service.uploadImage(file);  // 이미지 업로드
        String thumbnailUrl = s3Service.generateThumbnail(file);  // 썸네일 생성

        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", imageUrl);
        response.put("thumbnailUrl", thumbnailUrl);

        return ResponseEntity.ok(response);
    }
}
