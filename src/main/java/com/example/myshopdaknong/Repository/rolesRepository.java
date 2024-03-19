package com.example.myshopdaknong.Repository;

import com.example.myshopdaknong.Entity.roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface rolesRepository extends JpaRepository<roles,Integer> {
}
