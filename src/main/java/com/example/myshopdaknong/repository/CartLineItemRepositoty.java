package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Cart;
import com.example.myshopdaknong.entity.CartLineItems;
import com.example.myshopdaknong.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartLineItemRepositoty extends JpaRepository<CartLineItems,Integer> {
    public CartLineItems findByCartId_Id(Integer cartId_id);
    public CartLineItems findByProductId(Product productId);

    public List<CartLineItems> findByCartId(Cart cartId);

}
