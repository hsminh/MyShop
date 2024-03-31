package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Order;
import com.example.myshopdaknong.entity.OrderLineItem;
import com.example.myshopdaknong.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineItemRepository extends JpaRepository<OrderLineItem,Integer> {
    public List<OrderLineItem> findByOrderId(Order orderId);
//    public Order findByProductId(Product productId);



    @Query("SELECT p FROM Product p " +
            "JOIN OrderLineItem oli ON p.id = oli.productId.id " +
            "GROUP BY p.id " +
            "ORDER BY SUM(oli.quantity) DESC")
    List<Product> findProductsOrderedMost(Pageable pageable);
}
