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
    public List<ChatRoomDTO> selectAllLive() {
        List<ChatRoomDTO> chatRooms = new ArrayList<>(chatRoomDTOMap.values());
        Collections.reverse(chatRooms);

        return chatRooms;
    }

    // roomId 기준 채팅방 검색
    public ChatRoomDTO findRoomById(String roomId) {
        return chatRoomDTOMap.get(roomId);
    }

    // title 기준 채팅방 검색

    // 채팅방 생성


    // 채팅방 입장 - 인원 + 1

    // 채팅방 퇴장 - 인원 - 1

}
