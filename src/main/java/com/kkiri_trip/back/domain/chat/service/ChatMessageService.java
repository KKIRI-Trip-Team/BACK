package com.kkiri_trip.back.domain.chat.service;

import com.kkiri_trip.back.api.dto.ChatMessage.ChatMessageDto;
import com.kkiri_trip.back.domain.chat.entity.ChatMessage;
import com.kkiri_trip.back.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
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