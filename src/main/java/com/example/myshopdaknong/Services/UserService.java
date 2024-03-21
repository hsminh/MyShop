package com.example.myshopdaknong.Services;

import com.example.myshopdaknong.Entity.Users;
import com.example.myshopdaknong.Exception.UserNotFoundException;
import com.example.myshopdaknong.Repository.UserRepository;
import com.example.myshopdaknong.Repository.rolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private rolesRepository rolesRepository;

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
        users.setPassword(this.bCryptPasswordEncoder.encode(users.getPassword()));
        users.addRoles(rolesRepository.findById(2).get());
        if(users.getId()!=null)
        {
            users.setUpdatedAt(new Date());
        }else
        {
            users.setCreatedAt(new Date());
        }
        return this.userRepository.save(users);
    }
    public Users findUserById(int id) throws UserNotFoundException {
        try {
            return this.userRepository.findById(id).get();
        }catch (Exception ex)
        {
            throw new UserNotFoundException("Cannot Find User With Id : "+id);
        }
    }
}
