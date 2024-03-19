package com.example.myshopdaknong.Repository;

import com.example.myshopdaknong.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategory extends JpaRepository<Product,Integer> {
}
