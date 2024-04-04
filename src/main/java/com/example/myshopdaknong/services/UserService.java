package com.example.myshopdaknong.services;

import com.example.myshopdaknong.entity.Role;
import com.example.myshopdaknong.entity.UserProfile;
import com.example.myshopdaknong.entity.User;
import com.example.myshopdaknong.exception.UserNotFoundException;
import com.example.myshopdaknong.repository.UserProfileRepository;
import com.example.myshopdaknong.repository.UserRepository;
import com.example.myshopdaknong.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository rolesRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;


    public String checkUserNameUni(String userName)
    {
        User users=this.userRepository.findUsersByUserName(userName);
        if(users!=null)
        {
            return "duplicated";
        }
        return "ok";
    }

    public User findUserByUserName(String userName)
    {
        return this.userRepository.findUsersByUserName(userName);
    }

    public User save(User users) {
        Role defaultRole = rolesRepository.findById(1).orElseGet(() -> createDefaultRole(1));
        users.addRoles(defaultRole);

        if (users.getId() != null) {
            users.setUpdatedAt(new Date());
        } else {
            users.setCreatedAt(new Date());
        }

        return this.userRepository.save(users);
    }

    private Role createDefaultRole(int roleId) {
        String roleName;
        String roleDescription;

        if (roleId == 1) {
            roleName = "Admin";
            roleDescription = "Do Anything";
        } else if (roleId == 2) {
            roleName = "User";
            roleDescription = "User role is for those who engage in buying and selling goods";
        } else {
            roleName = "Unknown Role";
            roleDescription = "Unknown description";
        }

        Role defaultRole = new Role(roleName, roleDescription);
        defaultRole.setId(roleId);
        return this.rolesRepository.save(defaultRole);
    }



    public UserProfile saveUserProfile(UserProfile userProfile)
    {
        return this.userProfileRepository.save(userProfile);
    }

    public User findUserById(int id) throws UserNotFoundException {
        try {
            return this.userRepository.findById(id).get();
        }catch (Exception ex)
        {
            throw new UserNotFoundException("Cannot Find User With Id : "+id);
        }
    }

    public UserProfile getUserProfileByUsersId(Integer id) {
        UserProfile userProfile=this.userProfileRepository.getUserProfileByUsers_Id(id);
        return userProfile;
    }

    public Optional<UserProfile> getUserProfileById(Integer id) {
        return this.userProfileRepository.findById(id);
    }

    public User createNewUser(User newUser) {
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        newUser.setCreatedAt(new Date());
        return newUser;
    }

    public User updateUser(String editPassword, User editedUser) throws UserNotFoundException {
        User user = this.findUserById(editedUser.getId());
        user.setActive(editedUser.getActive());
        user.setUpdatedAt(new Date());
        user.setFirstName(editedUser.getFirstName());
        user.setLastName(editedUser.getLastName());
        if (editPassword != null && !editPassword.isEmpty()) {
            user.setPassword(this.bCryptPasswordEncoder.encode(editPassword));
        } else {
            user.setPassword(user.getPassword());
        }
        return user;
    }

    public void updateProfile(UserProfile userProfile, UserDetails userDetails) {
        if (userProfile.getUsers() == null) {
            User loggedInUser = this.findUserByUserName(userDetails.getUsername());
            userProfile.setUsers(loggedInUser);
        }
        if (userProfile.getId() == null || userProfile.getId() == 0) {
            userProfile.setCreatedAt(new Date());
            this.saveUserProfile(userProfile);
        } else {
            UserProfile userProfileInData = this.getUserProfileById(userProfile.getId()).orElse(null);
            if (userProfileInData != null) {
                userProfileInData.setUpdatedAt(new Date());
                userProfileInData.setGender(userProfile.isGender());
                userProfileInData.setPhoneNumber(userProfile.getPhoneNumber());
                userProfileInData.setAddress(userProfile.getAddress());
                userProfileInData.setBio(userProfile.getBio());
                this.saveUserProfile(userProfileInData);
            }
        }
    }

    public void prepareFormModel(Model model, String pageTitle, boolean isNewUser) {
        model.addAttribute("isUpdateUser", true);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("titleForm", pageTitle);
        model.addAttribute("isNewUser", isNewUser);
    }
}
