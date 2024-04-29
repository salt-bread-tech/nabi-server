package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.CreateChatRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetChatResponse;
import tech.bread.solt.doctornyangserver.service.NabiService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class NabiController {

    final NabiService nabiService;

    @Autowired
    public NabiController(NabiService nabiService) {
        this.nabiService = nabiService;
    }

    @GetMapping("/chats/{uid}")
    public List<GetChatResponse> getChats(@PathVariable("uid") int uid) {
        return nabiService.getChats(uid);
    }

    @GetMapping("/chats/{uid}/{page}")
    public List<GetChatResponse> getChats(@PathVariable("uid") int uid, @PathVariable("page") int page) {
        return nabiService.getChats(uid, page);
    }

    @PostMapping("/chat")
    public String createChat(@RequestBody CreateChatRequest request) {
        return nabiService.createChat(request);
    }

    @GetMapping("/feed/{uid}")
    public int feed(@PathVariable("uid") int uid) {
        return nabiService.feed(uid);
    }
}
