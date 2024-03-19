package com.example.myshopdaknong.Service;

import com.example.myshopdaknong.Entity.Users;
import com.example.myshopdaknong.Repository.UserRepository;
import org.apache.catalina.User;
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
        if(users.getId()!=null)
        {
            users.setUpdatedAt(new Date());
        }else
        {
            users.setCreatedAt(new Date());
        }
        return this.userRepository.save(users);
    }
}
