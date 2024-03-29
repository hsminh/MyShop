package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Cart;
import com.example.myshopdaknong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartReposttory extends JpaRepository<Cart,Integer> {
    public Cart findByUsersId(User usersId);
}
