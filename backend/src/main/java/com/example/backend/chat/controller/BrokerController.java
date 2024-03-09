package com.example.backend.chat.controller;

import com.example.backend.chat.model.MessageDto;
import com.example.backend.chat.model.SocketDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/test")
public class BrokerController {
    private final SimpMessagingTemplate template;

    @MessageMapping("/receive")
    @SendTo("/send")
    public SocketDto socketHandler(SocketDto socketDto) {
        String userName = socketDto.getUserName();
        String content = socketDto.getContent();
        log.info("userName = {}", userName);
        log.info("content = {}", content);

        return new SocketDto(userName, content);
    }

    @MessageMapping("/room/{roomId}/entered")
    public void entered(@DestinationVariable(value = "roomId") String roomId, MessageDto message) {
        log.info("roomId = {}", roomId);
        log.info("message = {}", message);

        final String payload = message.getWriter() + "님이 입장하였습니다.";
        template.convertAndSend("/sub/room/" + roomId, payload);
    }

    @MessageMapping("/room/{roomId}")
    public void sendMessage(@DestinationVariable(value = "roomId") String roomId, MessageDto message) {
        log.info("roomId = {}", roomId);
        log.info("message = {}", message);

        template.convertAndSend("/sub/room/" + roomId, message.getMessage());
    }
}
