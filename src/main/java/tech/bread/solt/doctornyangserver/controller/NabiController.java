package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.bread.solt.doctornyangserver.model.dto.request.CreateChatRequest;
import tech.bread.solt.doctornyangserver.service.NabiService;

@RestController
public class NabiController {

    final NabiService nabiService;

    @Autowired
    public NabiController(NabiService nabiService) {
        this.nabiService = nabiService;
    }

    @PostMapping("/chat")
    public String createChat(@RequestBody CreateChatRequest request) {
        return nabiService.createChat(request);
    }

}
