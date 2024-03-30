package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Token;
import com.example.myshopdaknong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {
    Token findByToken(String tokenValue);
    List<Token>findByUser(User user);
}
