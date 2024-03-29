package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    public User findUsersByUserName(String userName);
}
