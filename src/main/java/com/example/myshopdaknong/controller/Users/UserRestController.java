package com.example.myshopdaknong.controller.Users;

import com.example.myshopdaknong.entity.Token;
import com.example.myshopdaknong.entity.User;
import com.example.myshopdaknong.services.TokenService;
import com.example.myshopdaknong.services.UserService;
import com.example.myshopdaknong.util.EmailSender;
import com.example.myshopdaknong.util.GenarateRandomNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
        @GetMapping("/users/`check`-username-unique")
        public String checkUserNameUni(@RequestParam("username") String userName)
        {
            return this.userService.checkUserNameUni(userName);
        }
        @GetMapping("/users/send-email")
        public String senEmail(@RequestParam("email") String email)
        {
            User accountForgot=this.userService.findUserByUserName(email);
            if(accountForgot!=null)
            {
                String token=GenarateRandomNumber.generateRandomNumberString();
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
        @GetMapping("/users/verify-verification-code")
        public String verifyVerificationCode(@RequestParam("digit1") String digit1,
                                             @RequestParam("digit2") String digit2,
                                             @RequestParam("digit3") String digit3,
                                             @RequestParam("digit4") String digit4,
                                             @RequestParam("digit5") String digit5,
                                             @RequestParam("digit6") String digit6,
                                             @RequestParam("email") String email) {

            String code = digit1 + digit2 + digit3 + digit4 + digit5 + digit6;
            User verifiedUser = this.userService.findUserByUserName(email);

            Token token=this.tokenService.findByToken(code);

            if (this.tokenService.isValidToken(code)&&token.getToken().equals(code)&&token.getUser().equals(verifiedUser)) {
                return code;
            } else {
                return "duplicated";
            }
        }

    }
