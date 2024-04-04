package com.example.myshopdaknong.controller.Users;

import com.example.myshopdaknong.entity.Token;
import com.example.myshopdaknong.entity.UserProfile;
import com.example.myshopdaknong.entity.User;
import com.example.myshopdaknong.exception.UserNotFoundException;
import com.example.myshopdaknong.sercurity.ShopMeUserDetail;
import com.example.myshopdaknong.services.TokenService;
import com.example.myshopdaknong.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
public class UsersController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/users/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("pageTitle", "Register Form");
        model.addAttribute("titleForm", "Sign Up");
        model.addAttribute("isNewUser", true);
        model.addAttribute("users", new User());
        return "users/register-form";
    }

    public void setUpToUpdateForm(Model model,UserProfile existingUserProfile)
    {
        model.addAttribute("pageTitle", "Update User");
        model.addAttribute("titleForm", "Update User Profile");
        if (existingUserProfile == null) {
            UserProfile userProfile = new UserProfile();
            model.addAttribute("isCheckGenderChoose", false);
            model.addAttribute("userProfile", new UserProfile());
        } else {
            model.addAttribute("isCheckGenderChoose", true);
            model.addAttribute("userProfile", existingUserProfile);
        }
    }
    @GetMapping("/users/update_information")
    public String formUpdateInformation(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        User loggedInUser = null;
        if (userDetails != null) {
            loggedInUser = userService.findUserByUserName(userDetails.getUsername());
        }
        UserProfile existingUserProfile = this.userService.getUserProfileByUsersId(loggedInUser.getId());
        this.setUpToUpdateForm(model,existingUserProfile);
        return "users/update-information-user";
    }

    @PostMapping("/users/update_information/save")
    public String updateInformation(@AuthenticationPrincipal UserDetails userDetails, @Valid UserProfile userProfile, BindingResult bindingResult,Model model,RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors())
        {
            this.setUpToUpdateForm(model,userProfile);
            return "users/update-information-user";
        }
        this.userService.updateProfile(userProfile,userDetails);
        redirectAttributes.addFlashAttribute("messageSuccessfully","update Profile Successfully");
        return "redirect:/main-page";
    }



    @GetMapping("/users/edit")
    public String showEditForm(@AuthenticationPrincipal ShopMeUserDetail userDetails, Model model) {
        if (userDetails != null) {
            User user = userService.findUserByUserName(userDetails.getUsername());
            this.userService.prepareFormModel(model, "Edit User", false);
            model.addAttribute("pageTitle", "Edit User");
            model.addAttribute("titleForm", "Edit User");
            model.addAttribute("users", user);
            return "users/register-form";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/users/forgot")
    public String showForgotPassWordForm(Model model)
    {
        model.addAttribute("pageTitle", "Forgot Password");
        model.addAttribute("users", new User());
        return "users/forgot";
    }

    @GetMapping("/users/verify")
    public String showVerificationForm(@RequestParam("email") String email, Model model) {
        model.addAttribute("pageTitle", "Verification Code");
        model.addAttribute("email",email);
        return "users/vertification-code-form";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Token resetToken = this.tokenService.findByToken(token);
        if (resetToken == null) {
            return "redirect:/users/forgot";
        }
        model.addAttribute("token", token);
        return "users/update-password";
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
        return "users/update-password";
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
            redirectAttributes.addFlashAttribute("message","Change Password Successfully");
            return "login-form";
        }
        return "redirect:/users/forgot";
    }



    @PostMapping("/users/save")
    public String saveUser(@RequestParam(value = "editPassword", required = false) String editPassword, @Valid  @ModelAttribute("users") User users, BindingResult bindingResult, Model model) throws UserNotFoundException {
        if (bindingResult.hasErrors()) {
            if (users.getId() != null) {
                this.userService.prepareFormModel(model, "Edit User", false);
            } else {
                this.userService.prepareFormModel(model, "Sign Up", true);
            }
            return "users/register-form";
        }
        User userToSave;
        if (users.getId() != null && users.getId() != 0) {
            userToSave = this.userService.updateUser(editPassword, users);
        } else {
            userToSave = this.userService.createNewUser(users);
        }
        this.userService.save(userToSave);
        return "redirect:/login-form";
    }
}