package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Order;
import com.example.myshopdaknong.entity.OrderLineItem;
import com.example.myshopdaknong.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem,Integer> {
    public Page<OrderLineItem> findByOrderId(Order orderId, Pageable pageable);
//    public Order findByProductId(Product productId);



    @Query("SELECT p, SUM(oli.quantity) AS totalOrdered " +
            "FROM Product p " +
            "JOIN OrderLineItem oli ON p.id = oli.productId.id " +
            "WHERE p.isActive = true " +
            "GROUP BY p.id " +
            "ORDER BY totalOrdered DESC")

    List<Object[]> findProductsOrderedMost(Pageable pageable);
//    @Query("SELECT p FROM Product p " +
//            "JOIN OrderLineItem oli ON p.id = oli.productId.id " +
//            "GROUP BY p.id " +
//            "ORDER BY SUM(oli.quantity) DESC")
//    List<Product> findProductsOrderedMost(Pageable pageable);

}
