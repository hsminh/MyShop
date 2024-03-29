package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Cart;
import com.example.myshopdaknong.entity.CartLineItem;
import com.example.myshopdaknong.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartLineItemRepositoty extends JpaRepository<CartLineItem,Integer> {
    public CartLineItem findByCartId_Id(Integer cartId_id);
    public CartLineItem findByProductId(Product productId);

    public List<CartLineItem> findByCartId(Cart cartId);



}
