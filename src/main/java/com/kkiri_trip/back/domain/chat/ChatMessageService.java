package com.kkiri_trip.back.domain.chat;

import com.kkiri_trip.back.api.dto.ChatMessage.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(ChatMessageDto message) {
        ChatMessage chatMessage = ChatMessage.builder()
                .id(message.getId())
                .chatRoomId(message.getChatRoomId())
                .sender(message.getSender())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .messageType(message.getMessageType())
                .imageUrl(message.getImageUrl())
                .thumbnailUrl(message.getThumbnailUrl())
                .build();
        return chatMessageRepository.save(chatMessage);
    }
}