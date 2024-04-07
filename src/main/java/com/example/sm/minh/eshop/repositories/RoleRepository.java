package com.example.sm.minh.eshop.repositories;

import com.example.sm.minh.eshop.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
//    public Roles findByName(String Name);
}
