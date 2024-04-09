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
    @Autowired
    private TokenService tokenService;
        @GetMapping("/users/check-username-unique")
        public String checkUserNameUnique(@RequestParam("username") String userName)
        {
            return this.userService.checkUserNameUni(userName);
        }
            @GetMapping("/users/send-email")
            public String sendEmail(@RequestParam("email") String email)
            {
                User accountForgot=this.userService.findUserByUserName(email);
                if(accountForgot!=null)
                {
                    String token= GenerateRandomNumber.generateRandomNumberString();
                    String from = "kucantscute@gmail.com";
                    String password = "ugop edsx ieoo fecs";
                    String to = email;
                    //Content
                    String subject ="Your Verification Code";
                    String content = "Hello : "  + accountForgot.getFullName() + ", here is your verification code: " + token;
                    EmailSender.sendEmail(from,password,to,subject,content);
                    tokenService.deleteToken(accountForgot);
                    tokenService.createToken(accountForgot,token);
                    return "ok";
                }
                return "duplicated";
            }


    }
