package com.example.myshopdaknong.services;

import com.example.myshopdaknong.entity.UserProfile;
import com.example.myshopdaknong.entity.Users;
import com.example.myshopdaknong.exception.UserNotFoundException;
import com.example.myshopdaknong.repository.UserProfileRepository;
import com.example.myshopdaknong.repository.UserRepository;
import com.example.myshopdaknong.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    public String checkUserNameUni(String userName)
    {
        Users users=this.userRepository.findUsersByUserName(userName);
        if(users!=null)
        {
            return "duplicated";
        }
        return "ok";
    }

    public Users findUserByUserName(String userName)
    {
        return this.userRepository.findUsersByUserName(userName);
    }

    public Users save(Users users)
    {
        users.addRoles(rolesRepository.findById(2).get());
        users.setPassword(this.bCryptPasswordEncoder.encode(users.getPassword()));
        if(users.getId()!=null)
        {
            users.setUpdatedAt(new Date());
        }else
        {
            users.setCreatedAt(new Date());
        }
        return this.userRepository.save(users);
    }

    public UserProfile saveUserProfile(UserProfile userProfile)
    {
        return this.userProfileRepository.save(userProfile);
    }

    public Users findUserById(int id) throws UserNotFoundException {
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
}
