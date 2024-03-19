package com.example.myshopdaknong.Controller.Users;

import com.example.myshopdaknong.Entity.Users;
import com.example.myshopdaknong.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class UsersController {
    @Autowired
    private UserService userService;
    @GetMapping("/users/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("pageTitle","Register Form");
        model.addAttribute("Users",new Users());
        return "Users/RegisterForm";
    }
    @PostMapping("/users/save")
    public String saveUser(Users Users, Model model) {
        Users.setCreatedAt(new Date());
        this.userService.save(Users);
        return "redirect:/login-form";
    }

}
