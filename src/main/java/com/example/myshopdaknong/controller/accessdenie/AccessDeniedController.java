package com.example.myshopdaknong.controller.accessdenie;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccessDeniedController {

    @RequestMapping("/access-denied")
    public String accessDenied() {
        return "denied/access-denied";
    }
}
