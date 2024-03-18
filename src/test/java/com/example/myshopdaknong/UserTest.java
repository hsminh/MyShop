package com.example.myshopdaknong;

import com.example.myshopdaknong.Entity.Users;
import com.example.myshopdaknong.Repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    public void CteateNewUser()
    {
        Users users=new Users("hosyminh1182004@gmail.com","123","Ho","minh");
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
}
