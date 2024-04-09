package com.example.sm.minh.eshop.controllers.Users;

import com.example.sm.minh.eshop.models.User;
import com.example.sm.minh.eshop.services.TokenService;
import com.example.sm.minh.eshop.services.UserService;
import com.example.sm.minh.eshop.utilities.EmailSender;
import com.example.sm.minh.eshop.utilities.GenerateRandomNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
    @Autowired
    private UserService userService;
        @GetMapping("/users/check-username-unique")
        public String checkUserNameUnique(@RequestParam("username") String userName)
        {
            return this.userService.checkUserNameUni(userName);
        }
    }
