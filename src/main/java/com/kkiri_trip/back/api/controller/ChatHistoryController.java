package com.kkiri_trip.back.api.controller;

import com.kkiri_trip.back.domain.mongo.chat.service.ChatHistoryService;
import com.kkiri_trip.back.domain.mongo.chat.entity.ChatMessage;
import com.kkiri_trip.back.global.common.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatHistoryController {


    private final ChatHistoryService chatHistoryService;
    @GetMapping("/{chatRoomId}/messages")
    public ResponseEntity<ApiResponseDto<List<ChatMessage>>> getChatHistory(@PathVariable Long chatRoomId) {
        System.out.println("룸 아이디 >>>>>>>>>>>>> " + chatRoomId);

        List<ChatMessage> messages = chatHistoryService.getlist(chatRoomId);
        return ApiResponseDto.from(HttpStatus.OK, "채팅 내역 조회", messages);
    }

}
