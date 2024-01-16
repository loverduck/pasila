package com.ssafy.WebRTC.Live.Controller;

import com.ssafy.WebRTC.Live.DAO.ChatDAO;
import com.ssafy.WebRTC.Live.DTO.ChatRoomDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class ChatRoomController {

    @Autowired
    private ChatDAO chatDAO;

    // 라이브 리스트
    @GetMapping("/")
    public String getLiveList(Model model) {
        model.addAttribute("liveList", chatDAO.getLiveList());
        log.info("GET All Live List: {}", chatDAO.getLiveList());
        return "liveList";
    }

    // 라이브 방송 시작(채팅)
    @PostMapping("/chat/on")
    public String startLive(@RequestParam String title, RedirectAttributes rttr){
        ChatRoomDTO room = chatDAO.createChatRoom(title);
        log.info("CREATE Live: {}", room);
        rttr.addFlashAttribute("room", room);
        return "redirect:/";
    }

    // 라이브 입장(채팅)
    @GetMapping("/chat/enter")
    public String enterLive(Model model, String roomId){
        log.info("LiveId: {}", roomId);
        model.addAttribute("room", chatDAO.findRoomById(roomId));
        return "chatroom";
    }

}