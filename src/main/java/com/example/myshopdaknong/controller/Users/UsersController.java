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
    public String updateInforUser(@AuthenticationPrincipal UserDetails userDetails,Model model) {
        model.addAttribute("pageTitle","Update User");
        model.addAttribute("TitleForm", "Update User Profile");
        Users userLogin=null;
        if(userDetails!=null)
        {
            userLogin = userService.findUserByUserName(userDetails.getUsername());
        }
        UserProfile isEsixtUserProfile=this.userService.getUserProfileByUsersId(userLogin.getId());
        if(isEsixtUserProfile==null)
        {
            UserProfile userProfile=new UserProfile();
            model.addAttribute("isCheckGenderChoose",false);
            model.addAttribute("userProfile",new UserProfile());
        }else
        {
            model.addAttribute("isCheckGenderChoose",true);
            model.addAttribute("userProfile",isEsixtUserProfile);
        }
        return "users/update-infor-user";
    }

    @PostMapping("/users/user-infor/save")
    public String saveUpdateInfor(@AuthenticationPrincipal UserDetails userDetails,UserProfile userProfile,Model model) {
        if(userProfile.getUsers()==null)
        {
            Users userLogin = userService.findUserByUserName(userDetails.getUsername());
            userProfile.setUsers(userLogin);
        }
        if(userProfile.getId()==null||userProfile.getId()==0)
        {
            userProfile.setCreatedAt(new Date());
            this.userService.saveUserProfile(userProfile);

        }else
        {
            UserProfile userProfileInData=this.userService.getUserProfileById(userProfile.getId()).get();
            userProfileInData.setUpdatedAt(new Date());
            userProfileInData.setGender(userProfile.isGender());
            userProfileInData.setPhoneNumber(userProfile.getPhoneNumber());
            userProfileInData.setAddress(userProfile.getAddress());
            userProfileInData.setBio(userProfile.getBio());
            this.userService.saveUserProfile(userProfileInData);
        }
        return "redirect:/main-page";
    }
    @GetMapping("/users/edit")
    public String showEditForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            Users user = userService.findUserByUserName(userDetails.getUsername());
            System.out.println(user);
            model.addAttribute("isUpdateUser", true);
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
