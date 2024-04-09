package com.example.sm.minh.eshop.controllers.AuthController;

import com.example.sm.minh.eshop.models.Token;
import com.example.sm.minh.eshop.models.User;
import com.example.sm.minh.eshop.services.TokenService;
import com.example.sm.minh.eshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class authRestController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @GetMapping("/auth/verify-verification-code")
    public String verifyVerificationCode(@RequestParam("verificationCode") String verificationCode,
                                         @RequestParam("email") String email) {
        User verifiedUser = this.userService.findUserByUserName(email);
        Token token=this.tokenService.isTokenExists(verificationCode);
        if (this.tokenService.isValidToken(verificationCode)&&token.getToken().equals(verificationCode)&&token.getUser().equals(verifiedUser)) {
            return verificationCode;
        } else {
            return "duplicated";
        }
    }
}
