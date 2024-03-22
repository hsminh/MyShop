package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartReposttory extends JpaRepository<Cart,Integer> {
}
