package com.example.myshopdaknong.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String MainFile()
    {
        return "index";
    }

    @GetMapping("/loginform")
    public String LoginForm()
    {
        return "LoginForm";
    }
}
