package com.example.myshopdaknong.Controller.Users;

import com.example.myshopdaknong.Entity.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {
    @GetMapping("users/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("pageTitle","Register Form");
        model.addAttribute("Users",new Users());
        return "Users/RegisterForm";
    }
}
