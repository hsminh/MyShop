package com.example.myshopdaknong;

import com.example.myshopdaknong.Entity.roles;
import com.example.myshopdaknong.Repository.rolesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootTest
public class RolesTest {
    @Autowired
    private rolesRepository rolesRepository;
    @Test
    public void createTwoRoles()
    {
        roles rolesAdmin=new roles("Admin","Do AnyThing");
        roles rolesUser=new roles("User","User role is for those who engage in buying and selling goods");
        roles rolesAdminSaved=this.rolesRepository.save(rolesAdmin);
        roles rolesUserSaved=this.rolesRepository.save(rolesUser);
        Assertions.assertTrue(rolesUserSaved.getId()!=null&&rolesAdminSaved.getId()!=null);
    }
}
