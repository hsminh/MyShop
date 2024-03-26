package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Product,Integer> {
    public List<Product> findProductByNameOrSku(String name, String sku);

    public Product findProductByName(String name);
    public Product findProductBySku(String sku);
    @Query("SELECT p FROM Product p JOIN p.ListProductCategories pc WHERE pc.id = :categoryId")
    List<Product> findAll(Integer categoryId);
    @Query("SELECT p FROM Product p JOIN p.ListProductCategories pc WHERE pc.id = :categoryId and p.name like %:keyword%")
    List<Product> findAll(Integer categoryId, String keyword);


    @Query("SELECT p FROM Product p where p.name like %:keyword%")
    List<Product> findAll(String keyword);
}
