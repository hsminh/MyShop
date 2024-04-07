package com.example.sm.minh.eshop.controllers.AuthController;

import com.example.sm.minh.eshop.entities.Token;
import com.example.sm.minh.eshop.entities.User;
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
    @GetMapping("/users/forgot")
    public String showForgotPassWordForm(Model model)
    {
        model.addAttribute("pageTitle", "Forgot Password");
        model.addAttribute("users", new User());
        return "user/forgot";
    }

    @GetMapping("/users/verify")
    public String showVerificationForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("pageTitle", "Verification Code");
        model.addAttribute("email",email);
        return "user/vertification-code-form";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Token resetToken = this.tokenService.findByToken(token);
        if (resetToken == null) {
            return "redirect:/users/forgot";
        }
        model.addAttribute("token", token);
        return "user/update-password";
    }

    @GetMapping("/users/update-password")
    public String showUpdatePasswordForm(@RequestParam("email")String email, @RequestParam("token")String code,Model model, RedirectAttributes redirectAttributes) {
        User verifiedUser = this.userService.findUserByUserName(email);
        Token token=this.tokenService.findByToken(code);

        if (!(this.tokenService.isValidToken(code)&&token.getToken().equals(code)&&token.getUser().equals(verifiedUser))) {
            return "redirect:/users/forgot";
        }

        model.addAttribute("pageTitle", "Change Password");
        model.addAttribute("email",email);
        model.addAttribute("token",token);
        return "user/update-password";
    }

    @PostMapping("/user/save-update-password")
    public String saveUpdatePassword(@RequestParam("email")String email
            , @RequestParam("password")String newPassword
            , @RequestParam("token")String token
            , RedirectAttributes redirectAttributes) {

        User user=this.userService.findUserByUserName(email);
        if(user!=null)
        {
            user.setPassword(passwordEncoder.encode(newPassword));
            this.tokenService.deleteToken(user);
            this.userService.save(user);
            redirectAttributes.addFlashAttribute("Message","Change Password Successfully");
            return "login-form";
        }
        return "redirect:/users/forgot";
    }
}
