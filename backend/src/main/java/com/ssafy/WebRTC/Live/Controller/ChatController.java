package com.ssafy.WebRTC.Live.Controller;


import com.ssafy.WebRTC.Live.DAO.ChatDAO;
import com.ssafy.WebRTC.Live.DTO.ChatDTO;
import com.ssafy.WebRTC.Live.DTO.ChatRoomDTO;
import com.ssafy.WebRTC.Live.DTO.ChatRoomMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;



import java.util.ArrayList;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    // 아래에서 사용되는 convertAndSend 를 사용하기 위해서 서언
    // convertAndSend 는 객체를 인자로 넘겨주면 자동으로 Message 객체로 변환 후 도착지로 전송한다.
    private final SimpMessageSendingOperations template;

    private final ChatDAO chatDAO;

    // MessageMapping 을 통해 webSocket 로 들어오는 메시지를 발신 처리한다.
    // 이때 클라이언트에서는 /pub/chat/message 로 요청하게 되고 이것을 controller 가 받아서 처리한다.
    // 처리가 완료되면 /sub/chat/room/roomId 로 메시지가 전송된다.
    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatDTO chat, SimpMessageHeaderAccessor headerAccessor) {

        // 채팅방 유저+1
        ChatDAO.plusUserCnt(chat.getRoomId());

        // 채팅방에 유저 추가 및 UserUUID 반환
        String userID = ChatDAO.addUser(ChatRoomMap.getInstance().getChatRooms(), chat.getRoomId(), chat.getSender());

        // 반환 결과를 socket session 에 userUUID 로 저장
        headerAccessor.getSessionAttributes().put("userID", userID);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

        chat.setMessage(chat.getSender() + " 님 입장!!");
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);

    }

    // 해당 유저
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatDTO chat) {
        log.info("CHAT {}", chat);
        chat.setMessage(chat.getMessage());
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);

    }

    // 유저 퇴장 시에는 EventListener 을 통해서 유저 퇴장을 확인
    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("DisConnEvent {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션에 있던 uuid 와 roomId 를 확인해서 채팅방 유저 리스트와 room 에서 해당 유저를 삭제
        String userID = (String) headerAccessor.getSessionAttributes().get("userID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAccessor {}", headerAccessor);

        // 채팅방 유저 -1
        ChatDAO.minusUserCnt(roomId);

        // 채팅방 유저 리스트에서 UUID 유저 닉네임 조회 및 리스트에서 유저 삭제
        String username = ChatDAO.findUserNameByRoomIdAndUserID(ChatRoomMap.getInstance().getChatRooms(), roomId, userID);
        ChatDAO.delUser(ChatRoomMap.getInstance().getChatRooms(), roomId, userID);

        if (username != null) {
            log.info("User Disconnected : " + username);

            // builder 어노테이션 활용
            ChatDTO chat = ChatDTO.builder()
                    .type(ChatDTO.MessageType.LEAVE)
                    .sender(username)
                    .message(username + " 님 퇴장!!")
                    .build();

            template.convertAndSend("/sub/chat/room/" + roomId, chat);
        }
    }

    // 채팅에 참여한 유저 리스트 반환
    @GetMapping("/chat/userlist")
    @ResponseBody
    public ArrayList<String> userList(String roomId) {

        return ChatDAO.getUserList(ChatRoomMap.getInstance().getChatRooms(), roomId);
    }

//    // 채팅에 참여한 유저 닉네임 중복 확인
//    @GetMapping("/chat/duplicateName")
//    @ResponseBody
//    public String isDuplicateName(@RequestParam("roomId") String roomId, @RequestParam("username") String username) {
//
//        // 유저 이름 확인
//        String userName = ChatDAO.isDuplicateName(ChatRoomMap.getInstance().getChatRooms(), roomId, username);
//        log.info("동작확인 {}", userName);
//
//        return userName;
//    }
// 채팅방 생성
// 채팅방 생성 후 다시 / 로 return
@PostMapping("/chat/createroom")
public String createRoom(@RequestParam("roomName") String name, RedirectAttributes rttr) {

    // log.info("chk {}", secretChk);

    // 매개변수 : 방 이름, 패스워드, 방 잠금 여부, 방 인원수
    ChatRoomDTO room;

    room = ChatDAO.createChatRoom(name);


    log.info("CREATE Chat Room [{}]", room);

    rttr.addFlashAttribute("roomName", room);
    return "redirect:/";
}

    // 채팅방 입장 화면
    // 파라미터로 넘어오는 roomId 를 확인후 해당 roomId 를 기준으로
    // 채팅방을 찾아서 클라이언트를 chatroom 으로 보낸다.
    @GetMapping("/chat/room")
    public String roomDetail(Model model, String roomId){

        log.info("roomId {}", roomId);


        ChatRoomDTO room = ChatRoomMap.getInstance().getChatRooms().get(roomId);

        model.addAttribute("room", room);

        return "chatroom";

    }


}
