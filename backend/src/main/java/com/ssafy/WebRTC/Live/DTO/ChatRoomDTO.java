package com.ssafy.WebRTC.Live.DTO;

import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

@Data
public class ChatRoomDTO {
    private String roomId; // 방송 번호
    private String title; // 방송 제목
    private long userCount; // 인원수

    private HashMap<String, String> userList = new HashMap<String, String>();

    public ChatRoomDTO create(String title) {
        ChatRoomDTO live = new ChatRoomDTO();
        live.roomId = UUID.randomUUID().toString();
        live.title = title;

        return live;
    }
}
