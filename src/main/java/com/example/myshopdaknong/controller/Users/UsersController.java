package com.example.myshopdaknong.controller.Users;

import com.example.myshopdaknong.entity.UserProfile;
import com.example.myshopdaknong.entity.Users;
import com.example.myshopdaknong.exception.UserNotFoundException;
import com.example.myshopdaknong.sercurity.ShopMeUserDetail;
import com.example.myshopdaknong.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
public class UsersController {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/users/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("pageTitle", "Register Form");
        model.addAttribute("titleForm", "Sign Up");
        model.addAttribute("isNewUser", true);
        model.addAttribute("users", new Users());
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

        Users loggedInUser = null;
        if (userDetails != null) {
            loggedInUser = userService.findUserByUserName(userDetails.getUsername());
        }
        UserProfile existingUserProfile = this.userService.getUserProfileByUsersId(loggedInUser.getId());
        this.setUpToUpdateForm(model,existingUserProfile);
        return "users/update-information-user";
    }

    @PostMapping("/users/update_information/save")
    public String updateInformation(@AuthenticationPrincipal UserDetails userDetails, @Valid UserProfile userProfile, BindingResult bindingResult,Model model) {
        if(bindingResult.hasErrors())
        {
            this.setUpToUpdateForm(model,userProfile);
            return "users/update-information-user";
        }
        if (userProfile.getUsers() == null) {
            Users loggedInUser = userService.findUserByUserName(userDetails.getUsername());
            userProfile.setUsers(loggedInUser);
        }
        if (userProfile.getId() == null || userProfile.getId() == 0) {
            userProfile.setCreatedAt(new Date());
            this.userService.saveUserProfile(userProfile);
        } else {
            UserProfile userProfileInData = this.userService.getUserProfileById(userProfile.getId()).orElse(null);
            if (userProfileInData != null) {
                userProfileInData.setUpdatedAt(new Date());
                userProfileInData.setGender(userProfile.isGender());
                userProfileInData.setPhoneNumber(userProfile.getPhoneNumber());
                userProfileInData.setAddress(userProfile.getAddress());
                userProfileInData.setBio(userProfile.getBio());
                this.userService.saveUserProfile(userProfileInData);
            }
        }
        return "redirect:/main-page";
    }

    public void setUpForEditOrRegister(Model model, String pageTitle, boolean isNewUser) {
        model.addAttribute("isUpdateUser", true);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("titleForm", pageTitle);
        model.addAttribute("isNewUser", isNewUser);
    }

    @GetMapping("/users/edit")
    public String showEditForm(@AuthenticationPrincipal ShopMeUserDetail userDetails, Model model) {
        if (userDetails != null) {
            Users user = userService.findUserByUserName(userDetails.getUsername());
            this.setUpForEditOrRegister(model, "Edit User", false);
            model.addAttribute("pageTitle", "Edit User");
            model.addAttribute("titleForm", "Edit User");
            model.addAttribute("users", user);
            return "users/register-form";
        } else {
            return "redirect:/login";
        }
    }

    public Users updateUser(String editPassword, Users editedUser) throws UserNotFoundException {
        Users user = this.userService.findUserById(editedUser.getId());
        user.setActive(editedUser.getActive());
        user.setUpdatedAt(new Date());
        user.setFirstName(editedUser.getFirstName());
        user.setLastName(editedUser.getLastName());
        if (editPassword != null && !editPassword.isEmpty()) {
            user.setPassword(this.passwordEncoder.encode(editPassword));
        } else {
            user.setPassword(user.getPassword());
        }
        return user;
    }

    public Users createNewUser(Users newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setCreatedAt(new Date());
        return newUser;
    }

    @PostMapping("/users/save")
    public String saveUser(@RequestParam(value = "editPassword", required = false) String editPassword, @Valid Users users, BindingResult bindingResult, Model model) throws UserNotFoundException {
        // Check if the form is invalid
        if (bindingResult.hasErrors()) {
            // Check if editing user
            if (users.getId() != null) {
                this.setUpForEditOrRegister(model, "Edit User", false);
            } else {
                // Check if creating new user
                this.setUpForEditOrRegister(model, "Sign Up", true);
            }
            return "users/register-form";
        }
        Users userToSave;
        if (users.getId() != null && users.getId() != 0) {
            userToSave = this.updateUser(editPassword, users);
        } else {
            userToSave = this.createNewUser(users);
        }
        this.userService.save(userToSave);
        return "redirect:/login-form";
    }
}
