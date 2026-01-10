package com.example.straffic.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping({"/", "/home"})
    public String home() {
        return "main";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
