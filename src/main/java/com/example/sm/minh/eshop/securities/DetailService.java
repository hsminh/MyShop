package com.example.sm.minh.eshop.securities;

import com.example.sm.minh.eshop.entities.User;
import com.example.sm.minh.eshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DetailService implements UserDetailsService {
    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("come this");
        System.out.println(username);

        User user=this.userService.findUserByUserName(username);
        System.out.println(user);
        if(user==null)
        {
            throw new UsernameNotFoundException("Cannot Find User With UserName "+username);
        }else {
            System.out.println(user);
            return new ShopMeUserDetail(user);
        }}

}
