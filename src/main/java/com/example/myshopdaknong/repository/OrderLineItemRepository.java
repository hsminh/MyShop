package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.CartLineItems;
import com.example.myshopdaknong.entity.Order;
import com.example.myshopdaknong.entity.OrderLineItem;
import com.example.myshopdaknong.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem,Integer> {
    public List<OrderLineItem> findByOrderId(Order orderId);
//    public Order findByProductId(Product productId);
}
