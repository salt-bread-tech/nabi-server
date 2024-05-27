package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.CreateChatRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.GetChatResponse;
import tech.bread.solt.doctornyangserver.service.NabiService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class NabiController {

    final NabiService nabiService;

    @Autowired
    public NabiController(NabiService nabiService) {
        this.nabiService = nabiService;
    }

    @GetMapping("/chats/recent")
    public List<GetChatResponse> getChats(Principal p) {
        return nabiService.getChats(p.getName());
    }

    @GetMapping("/chats/{page}")
    public List<GetChatResponse> getChatsByPage(@PathVariable("page") int page, Principal p) {
        return nabiService.getChats(p.getName(), page);
    }

    @PostMapping("/chat")
    public String createChat(@RequestBody CreateChatRequest request, Principal p) {
        return nabiService.createChat(request, p.getName());
    }

    @GetMapping("/feed")
    public int feed(Principal p) {
        return nabiService.feed(p.getName());
    }

    @GetMapping("/feed/reset")
    public String resetFeed() {
        return nabiService.resetFeed();
    }
}
