package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.bread.solt.doctornyangserver.model.dto.request.WidgetSequenceRequest;
import tech.bread.solt.doctornyangserver.model.dto.response.HomeResponse;
import tech.bread.solt.doctornyangserver.service.HomeService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {

    final HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/today")
    public HomeResponse getHomeData(Principal p) {
        return homeService.getHomeData(LocalDate.now(), p.getName());
    }

    @GetMapping("/{date}")
    public HomeResponse getHomeData(@PathVariable("date") LocalDate date, Principal p) {
        return homeService.getHomeData(date, p.getName());
    }

    @GetMapping("/widget")
    public Map<String, String> getWidgetSequence(Principal p) {
        return homeService.getWidgetSequence(p.getName());
    }

    @PutMapping("/widget")
    public String modifyWidgetSequence(@RequestBody WidgetSequenceRequest request, Principal p) {
        return homeService.modifyWidgetSequence(request, p.getName());
    }
}
