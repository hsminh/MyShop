package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Order;
import com.example.myshopdaknong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    public Order findByUsersId(User usersId);

}
