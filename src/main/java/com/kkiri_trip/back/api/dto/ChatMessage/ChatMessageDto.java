package com.kkiri_trip.back.api.dto.ChatMessage;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
    private String id;
    private Long chatRoomId;
    private String sender;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String messageType;
    private String imageUrl;       // 원본 이미지 URL
    private String thumbnailUrl;   // 썸네일 이미지 URL
}
