package com.kkiri_trip.back.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_messages")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
public class ChatMessage {
    @Id
    private String id;  // MongoDB의 문서 ID
    private Long chatRoomId;
    private String sender;
    private String content;
    private String messageType;
    private String imageUrl;       // 원본 이미지 URL
    private String thumbnailUrl;   // 썸네일 이미지 URL
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
