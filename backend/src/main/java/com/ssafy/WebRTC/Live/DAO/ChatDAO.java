package com.ssafy.WebRTC.Live.DAO;

import com.ssafy.WebRTC.Live.DTO.ChatRoomDTO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

// 추후 Service와 Repository(DAO)로 분리 예정
@Repository
@Slf4j
public class ChatDAO {
    private Map<String, ChatRoomDTO> chatRoomDTOMap;

    @PostConstruct
    private void init() {
        chatRoomDTOMap = new LinkedHashMap<>();
    }

    // 실시간 라이브 조회
    public List<ChatRoomDTO> getLiveList() {
        List<ChatRoomDTO> chatRooms = new ArrayList<>(chatRoomDTOMap.values());
        Collections.reverse(chatRooms);

        return chatRooms;
    }

    // roomId 기준 채팅방 검색
    public ChatRoomDTO findRoomById(String roomId) {
        return chatRoomDTOMap.get(roomId);
    }

    // 채팅방 생성
    public ChatRoomDTO createChatRoom(String title){
        // 채팅방 생성
        ChatRoomDTO chatRoom = new ChatRoomDTO().create(title);

        chatRoomDTOMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    // 채팅방 입장: 인원 + 1
    public void increaseUserCnt(String roomId) {
        ChatRoomDTO room = chatRoomDTOMap.get(roomId);
        room.setUserCount(room.getUserCount() + 1);
    }

    // 채팅방 퇴장: 인원 - 1
    public void decreaseUserCnt(String roomId){
        ChatRoomDTO room = chatRoomDTOMap.get(roomId);
        room.setUserCount(room.getUserCount() - 1);
    }

    // 시청자 명단에서 사용자 추가
    public String enterUser(String roomId, String userId, String userChannel){
        ChatRoomDTO room = chatRoomDTOMap.get(roomId);
        room.getUserList().put(userId, userChannel);

        return userId;
    }

    // 시청자 명단에서 사용자 삭제
    public String exitUser(String roomId, String userId){
        ChatRoomDTO room = chatRoomDTOMap.get(roomId);
        room.getUserList().remove(userId);

        return userId;
    }

    // 시청자 명단 조회
    public ArrayList<String> getUserList(String roomId){
        ArrayList<String> userList = new ArrayList<>();

        ChatRoomDTO room = chatRoomDTOMap.get(roomId);
        room.getUserList().forEach((k, v) -> userList.add(v));

        return userList;
    }
}
