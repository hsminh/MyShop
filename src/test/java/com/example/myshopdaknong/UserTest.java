package com.example.myshopdaknong;

import com.example.myshopdaknong.entity.Roles;
import com.example.myshopdaknong.entity.Users;
import com.example.myshopdaknong.repository.RolesRepository;
import com.example.myshopdaknong.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Test
    public void CteateNewUser()
    {
        Users users=new Users("admin","100","Ho","minh");
        users.setPassword(bCryptPasswordEncoder.encode("admin"));
//        Roles role=this.rolesRepository.findById(1).get();
//        users.setActive(true);
//        users.addRoles(role);
        Users SavedUser=this.userRepository.save(users);
        Assertions.assertTrue(SavedUser.getId()!=0);
    }
    @Test
    public void CreateMoreUsers()
    {
        List<Users>usersList=new ArrayList<>();
        for(int i=0;i<5;i++)
        {
            Users users=new Users("hosyminh1182004@gmail.com"+i,"123"+i,"Ho"+i,"minh"+i);
            usersList.add(users);
        }
        Assertions.assertTrue(this.userRepository.saveAll(usersList).size()!=0);
    }

    @Test
    public void set()
    {
        Users users=this.userRepository.findUsersByUserName("admin");
        System.out.println(users);
//        this.userRepository.save(users);
//        Assertions.assertTrue(this.userRepository.saveAll(usersList).size()!=0);
    }

    @Test
    public void SetActive()
    {
        Users users=this.userRepository.findById(1).get();
        users.setActive(true);
        this.userRepository.save(users);
//        Assertions.assertTrue(this.userRepository.saveAll(usersList).size()!=0);
    }
}
