package com.example.myshopdaknong.services;

import com.example.myshopdaknong.entity.Token;
import com.example.myshopdaknong.entity.User;
import com.example.myshopdaknong.repository.TokenRepository;
import com.example.myshopdaknong.util.GenarateRandomNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

// Service class to handle token-related operations
@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public Token createToken(User user,String code) {
        String tokenValue = code;


        LocalDateTime expirationDateTime = LocalDateTime.now().plusMinutes(5);

        Token token = new Token();
        token.setCreatedAt(LocalDateTime.now());
        token.setToken(tokenValue);
        token.setUser(user);
        token.setExpiresAt(expirationDateTime);
        tokenRepository.save(token);
        return token;
    }

    public boolean isValidToken(String tokenValue) {
        Token token = tokenRepository.findByToken(tokenValue);
        return token != null && !token.isExpired();
    }

    public void deleteToken(User user) {
        List<Token>tokenList =tokenRepository.findByUser(user);
        this.tokenRepository.deleteAll(tokenList);
    }


    public Token findByToken(String token) {
        return this.tokenRepository.findByToken(token);
    }
}
