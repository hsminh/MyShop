package com.example.myshopdaknong.Controller;

import com.example.myshopdaknong.Sercurity.DetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @GetMapping("/main-page")
    public String MainFile()
    {
        return "MainPage";
    }

    @GetMapping("/login-form")
    public String LoginForm(Model model, @RequestParam(value = "error", required = false) String error)
    {
        if(error!=null)
        {
            model.addAttribute("messageErr","Account or password is incorrect");
        }
        return "LoginForm";
    }
}
