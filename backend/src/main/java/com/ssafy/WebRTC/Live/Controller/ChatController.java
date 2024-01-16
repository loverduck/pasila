package com.ssafy.WebRTC.Live.Controller;

import com.ssafy.WebRTC.Live.DAO.ChatDAO;
import com.ssafy.WebRTC.Live.DTO.ChatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
    private final SimpMessageSendingOperations template;

    @Autowired
    ChatDAO chatDAO;

    // 사용자 입장
    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatDTO chat, SimpMessageHeaderAccessor headerAccessor){
        // 채팅방 입장: 인원 + 1
        chatDAO.increaseUserCnt(chat.getRoomId());

        // 시청자 명단에서 사용자 추가
        chatDAO.enterUser(chat.getRoomId(), chat.getUserId(), chat.getUserChannel());

        // 반환 결과 socket session에 저장
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());
        headerAccessor.getSessionAttributes().put("userId", chat.getUserId());

        // 입장메세지 전송
//        chat.setMessage(chat.getUserChannel() + "님이 입장하셨습니다.");
//        template.convertAndSend("/sub/chat/live" + chat.getRoomId(), chat);
    }

    // 사용자 퇴장
    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event){
        log.info("Disconnect {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        log.info("headerAccessor {}", headerAccessor);

        // 채팅방 퇴장: 인원 - 1
        chatDAO.decreaseUserCnt(roomId);

        // 시청자 명단에서 사용자 삭제
        chatDAO.exitUser(roomId, userId);
    }

    // 채팅 메세지 전송
    @MessageMapping("/chat/send")
    public void sendMessage(@Payload ChatDTO chat){
        log.info("ChatMessage {}", chat);
        chat.setMessage(chat.getMessage());
        template.convertAndSend("/sub/chat/live/" + chat.getRoomId(), chat);
    }

    // 라이브 시청자 명단
    @GetMapping("/chat/userlist")
    @ResponseBody
    public ArrayList<String> userList(String roomId) {
        return chatDAO.getUserList(roomId);
    }
}
