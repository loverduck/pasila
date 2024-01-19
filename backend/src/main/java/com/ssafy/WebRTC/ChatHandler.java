package com.ssafy.WebRTC;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.LinkedHashSet;

@Component
public class ChatHandler extends TextWebSocketHandler {

    private static LinkedHashSet<WebSocketSession> numSet = new LinkedHashSet<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //1 : 1 채팅이라 존재하는 부분
        //추후 스트리밍의 경우 각 방송마다 해당 방송에 참여한 사용자의 리스트 담아야하고 인원 제한도 존재하지 않는다.
        if(numSet.size()>=3){
            WebSocketSession oldSession = numSet.iterator().next();
            oldSession.sendMessage(new TextMessage("채팅이 종료되었습니다."));
            numSet.remove(numSet.iterator().next());
        }
        
        boolean isSessionAlive = false;
        
        // 해당 채팅창에 들어와 있는 유저의 경우 tf 판단
        for(WebSocketSession sess: numSet) {
            if(sess.getId().equals(session.getId())){
                isSessionAlive = true;
            }
        }
        if(isSessionAlive){
            for(WebSocketSession sess: numSet) {
                sess.sendMessage(message);
            }
        }
    }
    //방송에 참여
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        numSet.add(session);
    }
    //방송에서 퇴장
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        numSet.remove(session);
    }
}