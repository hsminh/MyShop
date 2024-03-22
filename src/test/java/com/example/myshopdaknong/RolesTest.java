package com.example.myshopdaknong;

import com.example.myshopdaknong.entity.Roles;
import com.example.myshopdaknong.repository.RolesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RolesTest {
    @Autowired
    private RolesRepository rolesRepository;
    @Test
    public void createTwoRoles()
    {
        Roles rolesAdmin=new Roles("Admin","Do AnyThing");
        Roles rolesUser=new Roles("User","User role is for those who engage in buying and selling goods");
        Roles rolesAdminSaved=this.rolesRepository.save(rolesAdmin);
        Roles rolesUserSaved=this.rolesRepository.save(rolesUser);
        Assertions.assertTrue(rolesUserSaved.getId()!=null&&rolesAdminSaved.getId()!=null);
    }
}
