package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Roles;
import com.example.myshopdaknong.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Integer> {
//    public Roles findByName(String Name);
}
