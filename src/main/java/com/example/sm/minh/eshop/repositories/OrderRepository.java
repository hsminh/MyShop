package com.example.sm.minh.eshop.repositories;

import com.example.sm.minh.eshop.models.Order;
import com.example.sm.minh.eshop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    public Order findByUserId(User userId);

}
