package com.kkiri_trip.back.api.dto;

import lombok.Data;

@Data
public class ChatMessage {
    private Long chatRoomId;  // 채팅방 ID
    private Long postId;      // 게시글 ID
    private String sender;    // 발신자
    private String content;   // 메시지 내용
    private Long timestamp;   // 메시지 전송 시간 (optional)
    private String messageType; // 메시지 타입 (이모지, 텍스트, 이미지 등)


}
