package com.ssafy.WebRTC.Live.DAO;

import com.ssafy.WebRTC.Live.DTO.ChatRoomDTO;
import com.ssafy.WebRTC.Live.DTO.ChatRoomMap;
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
    public static ChatRoomDTO createChatRoom(String title){
        ChatRoomDTO chatRoom = ChatRoomDTO.create(title);
        return  chatRoom;
    }

    // 채팅방 입장 - 인원 + 1
    public static void plusUserCnt(String roomId){
        log.info("cnt {}", ChatRoomMap.getInstance().getChatRooms().get(roomId).getUserCount());
        ChatRoomDTO room = ChatRoomMap.getInstance().getChatRooms().get(roomId);
        room.setUserCount(room.getUserCount()+1);
    }

    // 채팅방 퇴장 - 인원 - 1
    public static void minusUserCnt(String roomId){
        ChatRoomDTO room = ChatRoomMap.getInstance().getChatRooms().get(roomId);
        room.setUserCount(room.getUserCount()-1);
    }




    // 채팅방 유저 리스트에 유저 추가
    public static String addUser(Map<String, ChatRoomDTO> chatRoomMap, String roomId, String userName){
        ChatRoomDTO room = chatRoomMap.get(roomId);
        String userID = userName;

        // 아이디 중복 확인 후 userList 에 추가
        //room.getUserList().put(userUUID, userName);

        HashMap<String, String> userList = (HashMap<String, String>)room.getUserList();
        userList.put(userID, userName);


        return userID;
    }


    // 채팅방 userName 조회
    public static String findUserNameByRoomIdAndUserID(Map<String, ChatRoomDTO> chatRoomMap, String roomId, String userID){
        ChatRoomDTO room = chatRoomMap.get(roomId);
        return (String) room.getUserList().get(userID);
    }

    // 채팅방 전체 userlist 조회
    public static ArrayList<String> getUserList(Map<String, ChatRoomDTO> chatRoomMap, String roomId){
        ArrayList<String> list = new ArrayList<>();

        ChatRoomDTO room = chatRoomMap.get(roomId);

        // hashmap 을 for 문을 돌린 후
        // value 값만 뽑아내서 list 에 저장 후 reutrn
        room.getUserList().forEach((key, value) -> list.add((String) value));
        return list;
    }

    //채팅방에서 삭제
    public static void delUser(Map<String, ChatRoomDTO> chatRoomMap, String roomId, String userID){
        ChatRoomDTO room = chatRoomMap.get(roomId);
        room.getUserList().remove(userID);
    }



}
