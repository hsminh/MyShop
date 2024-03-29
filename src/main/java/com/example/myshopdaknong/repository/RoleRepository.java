package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
//    public Roles findByName(String Name);
}
