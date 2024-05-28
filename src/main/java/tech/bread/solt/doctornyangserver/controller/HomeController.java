package tech.bread.solt.doctornyangserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tech.bread.solt.doctornyangserver.model.dto.response.HomeResponse;
import tech.bread.solt.doctornyangserver.service.HomeService;

import java.security.Principal;
import java.time.LocalDate;

@RestController
public class HomeController {

    final HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/home/today")
    public HomeResponse getHomeData(Principal p) {
        return homeService.getHomeData(LocalDate.now(), p.getName());
    }

    @GetMapping("/home/{date}")
    public HomeResponse getHomeData(@PathVariable("date") LocalDate date, Principal p) {
        return homeService.getHomeData(date, p.getName());
    }
}
