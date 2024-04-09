package com.example.sm.minh.eshop.services;

import com.example.sm.minh.eshop.exceptions.TokenException;
import com.example.sm.minh.eshop.models.Token;
import com.example.sm.minh.eshop.models.User;
import com.example.sm.minh.eshop.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// Service class to handle token-related operations
@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public void createToken(User user, String code) {
        String tokenValue = code;
        LocalDateTime expirationDateTime = LocalDateTime.now().plusMinutes(5);
        Token token = new Token();
        token.setCreatedAt(LocalDateTime.now());
        token.setToken(tokenValue);
        token.setUser(user);
        token.setExpiresAt(expirationDateTime);
        tokenRepository.save(token);
    }

    public boolean isValidToken(Token token) {
        return token != null && !token.isExpired();
    }

    public void deleteToken(User user) {
        Token token=tokenRepository.findByUser(user);
        if(token!=null)
        {
            this.tokenRepository.delete(token);
        }
    }


    public Token isTokenExists(String token) {
        return this.tokenRepository.findByToken(token);
    }

    public Token findTokenByUser(User verifiedUser) throws TokenException {
        Token verificationToken=this.tokenRepository.findByUser(verifiedUser);
        if(verificationToken!=null)
        {
            return verificationToken;
        }
        throw new TokenException("Cannot Found Token");
    }
}
