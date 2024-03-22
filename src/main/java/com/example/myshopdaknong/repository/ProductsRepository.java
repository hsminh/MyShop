package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Product,Integer> {
    public List<Product> findProductByNameOrSku(String name, String sku);

    public Product findProductByName(String name);
    public Product findProductBySku(String sku);
}
