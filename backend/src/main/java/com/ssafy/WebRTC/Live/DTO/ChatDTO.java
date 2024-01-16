package com.ssafy.WebRTC.Live.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
    public enum MessageType {
        ENTER, SEND, LEAVE; // 입장, 채팅, 퇴장
    }

    private MessageType type; // 메세지 타입(입장, 전송, 퇴장)
    private String roomId; // 방 번호
    private String userId; // 작성자 아이디
    private String userChannel; // 작성자 채널명
    private String message; // 메세지 내용
    private String time; // 전송시간
}
