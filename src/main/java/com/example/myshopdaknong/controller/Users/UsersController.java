package com.example.myshopdaknong.controller.Users;

import com.example.myshopdaknong.entity.UserProfile;
import com.example.myshopdaknong.entity.Users;
import com.example.myshopdaknong.exception.UserNotFoundException;
import com.example.myshopdaknong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class UsersController {
    @Autowired
    private UserService userService;
    @GetMapping("/users/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("pageTitle","Register Form");
        model.addAttribute("TitleForm", "Sign Up");
        model.addAttribute("isNewUser", true);

        model.addAttribute("Users",new Users());
        return "users/register-form";
    }

    @GetMapping("/users/detail")
    public String updateInforUser(Model model) {

        model.addAttribute("pageTitle","Update User");
        model.addAttribute("TitleForm", "Update User");
        UserProfile userProfile=new UserProfile();
//        userProfile.setGender(null);
        model.addAttribute("userProfile",new UserProfile());
        return "users/update-infor-user";
    }

    @GetMapping("/users/edit")
    public String showEditForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            Users user = userService.findUserByUserName(userDetails.getUsername());
            System.out.println(user);
            model.addAttribute("pageTitle", "Edit User");
            model.addAttribute("TitleForm", "Edit User");
            // Xác định khi nào đang tạo mới
            model.addAttribute("isNewUser", false);
            model.addAttribute("Users", user);
            return "users/register-form";
        } else {
            // Redirect to login or handle the case where no user is authenticated
            return "redirect:/login";
        }
    }
    public Users EditUser(String editPassword,Users UsersEdit) throws UserNotFoundException {
        Users user=this.userService.findUserById(UsersEdit.getId());
        user.setActive(UsersEdit.getActive());
        user.setUpdatedAt(new Date());
        user.setFirstName(UsersEdit.getFirstName());
        user.setLastName(UsersEdit.getLastName());
        if(editPassword!=null&&!editPassword.isEmpty())
        {
            user.setPassword(editPassword);
        }
        return user;
    }
    public Users CreateNewUser(Users newUser) throws UserNotFoundException {
        newUser.setCreatedAt(new Date());
        return newUser;
    }
    @PostMapping("/users/save")
    public String saveUser(@RequestParam(value = "editPassword",required = false)String editPassword, Users Users, Model model) throws UserNotFoundException {
        Users users=null;
        System.out.println("this is infor "+ Users);
        if(Users.getId()!=null&&Users.getId()!=0)
        {
             users =this.EditUser(editPassword,Users);
        }else
        {
            System.out.println("come thisss");
             users=CreateNewUser(Users);
        }
        this.userService.save(users);
        return "redirect:/login-form";
    }

}
