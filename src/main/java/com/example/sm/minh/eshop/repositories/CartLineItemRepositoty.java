package com.example.sm.minh.eshop.repositories;

import com.example.sm.minh.eshop.entities.Cart;
import com.example.sm.minh.eshop.entities.CartLineItem;
import com.example.sm.minh.eshop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartLineItemRepositoty extends JpaRepository<CartLineItem,Integer> {
    public CartLineItem findByCartIdAndProductId(Cart cartId, Product productId);

//    @Query("select c.* from CartLineItem c where c.productId =:productId and c.productId.isActive=true")
    @Query("SELECT c FROM CartLineItem c WHERE c.productId = :productId AND c.productId.isActive = true")
    public List<CartLineItem> findByProductId(Product productId);
    @Query("SELECT c FROM CartLineItem c WHERE c.cartId = :cartId AND c.productId.isActive = true")
    public List<CartLineItem> findByCartId(Cart cartId);

}
