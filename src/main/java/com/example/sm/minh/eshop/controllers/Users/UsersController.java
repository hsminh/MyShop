package com.example.sm.minh.eshop.controllers.Users;

import com.example.sm.minh.eshop.entities.Token;
import com.example.sm.minh.eshop.entities.UserProfile;
import com.example.sm.minh.eshop.entities.User;
import com.example.sm.minh.eshop.exceptions.UserNotFoundException;
import com.example.sm.minh.eshop.securities.ShopMeUserDetail;
import com.example.sm.minh.eshop.services.TokenService;
import com.example.sm.minh.eshop.services.UserService;
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

@Controller
public class UsersController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/users/register")
    public String viewRegisterForm(Model model) {
        model.addAttribute("pageTitle", "Register Form");
        model.addAttribute("titleForm", "Sign Up");
        model.addAttribute("isNewUser", true);
        model.addAttribute("users", new User());
        return "user/register-form";
    }


    @GetMapping("/users/update_information")
    public String viewUpdateInformation(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        User userLoggin = null;
        if (userDetails != null) {
            userLoggin = userService.findUserByUserName(userDetails.getUsername());
        }
        UserProfile existingUserProfile = this.userService.getUserProfileByUsersId(userLoggin.getId());
        this.userService.setUpToUpdateForm(model,existingUserProfile);
        return "user/update-information-user";
    }

    @PostMapping("/users/update_information/save")
    public String updateInformation(@AuthenticationPrincipal UserDetails userDetails, @Valid UserProfile userProfile, BindingResult bindingResult,Model model,RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors())
        {
            this.userService.setUpToUpdateForm(model,userProfile);
            return "user/update-information-user";
        }
        this.userService.updateProfile(userProfile,userDetails);
        redirectAttributes.addFlashAttribute("Message","update Profile Successfully");
        return "redirect:/main-page";
    }


    @GetMapping("/users/edit")
    public String viewEditForm(@AuthenticationPrincipal ShopMeUserDetail userDetails, Model model) {
        if (userDetails != null) {
            User user = userService.findUserByUserName(userDetails.getUsername());
            this.userService.prepareFormModel(model, "Edit User", false);
            model.addAttribute("pageTitle", "Edit User");
            model.addAttribute("titleForm", "Edit User");
            model.addAttribute("users", user);
            return "user/register-form";
        } else {
            return "redirect:/login";
        }
    }


    @PostMapping("/users/save")
    public String saveUser(@RequestParam(value = "editPassword", required = false) String editPassword, @Valid  @ModelAttribute("users") User users, BindingResult bindingResult, Model model,RedirectAttributes redirectAttributes) throws UserNotFoundException {
        if (bindingResult.hasErrors()) {
            if (users.getId() != null) {
                this.userService.prepareFormModel(model, "Edit User", false);
            } else {
                this.userService.prepareFormModel(model, "Sign Up", true);
            }
            return "user/register-form";
        }
        User userToSave;
        if (users.getId() != null && users.getId() != 0) {
            userToSave = this.userService.updateUser(editPassword, users);
            redirectAttributes.addFlashAttribute("Message","Update Information Successfully");

        } else {
            userToSave = this.userService.createNewUser(users);
            redirectAttributes.addFlashAttribute("Message","Register Account Successfully");
        }
        this.userService.save(userToSave);
        return "redirect:/login-form";
    }
}