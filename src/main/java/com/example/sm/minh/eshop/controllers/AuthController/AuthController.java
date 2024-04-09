package com.example.sm.minh.eshop.controllers.AuthController;

import com.example.sm.minh.eshop.models.Token;
import com.example.sm.minh.eshop.models.User;
import com.example.sm.minh.eshop.services.TokenService;
import com.example.sm.minh.eshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @GetMapping("/auth/forgot-password")
    public String viewForgotPassword(Model model)
    {
        model.addAttribute("pageTitle", "Forgot Password");
        return "authenticated/forgot";
    }

    @GetMapping("/auth/verify-token")
    public String viewVerification(@RequestParam("email") String email, Model model) {
        model.addAttribute("pageTitle", "Verification Code");
        model.addAttribute("email",email);
        return "authenticated/vertification-code-form";
    }

    @GetMapping("/auth/reset-password")
    public String viewResetPassword(@RequestParam("token") String token, Model model) {
        Token resetToken = this.tokenService.isTokenExists(token);
        if (resetToken == null) {
            return "redirect:/auth/forgot-password";
        }
        model.addAttribute("token", token);
        return "authenticated/update-password";
    }

    @GetMapping("/auth/update-password")
    public String viewUpdatePassword(@RequestParam("email")String email, @RequestParam("token")String code,Model model) {
        User verifiedUser = this.userService.findUserByUserName(email);
        Token token=this.tokenService.isTokenExists(code);
        // Check token is valid 
        if (!(this.tokenService.isValidToken(code)&&token.getToken().equals(code)&&token.getUser().equals(verifiedUser))) {
            return "redirect:/auth/forgot";
        }
        model.addAttribute("pageTitle", "Change Password");
        model.addAttribute("email",email);
        model.addAttribute("token",token);
        return "authenticated/update-password";
    }

    @PostMapping("/auth/save-update-password")
    public String updatePassword(@RequestParam("email")String email
            , @RequestParam("password")String newPassword
            , RedirectAttributes redirectAttributes) {

        User savedUser=this.userService.findUserByUserName(email);
        if(savedUser!=null)
        {
            savedUser.setPassword(passwordEncoder.encode(newPassword));
            this.tokenService.deleteToken(savedUser);
            this.userService.save(savedUser);
            redirectAttributes.addFlashAttribute("Message","Change Password Successfully");
            return "login-form";
        }
        return "redirect:/auth/forgot";
    }
}
