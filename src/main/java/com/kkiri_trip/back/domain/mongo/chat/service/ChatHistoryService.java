package com.kkiri_trip.back.domain.mongo.chat.service;

import com.kkiri_trip.back.domain.mongo.chat.entity.ChatMessage;
import com.kkiri_trip.back.domain.mongo.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatHistoryService {

    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> getlist(Long chatRoomId)
    {
        return chatMessageRepository.findByChatRoomIdOrderByIdAsc(chatRoomId);
    }
}
