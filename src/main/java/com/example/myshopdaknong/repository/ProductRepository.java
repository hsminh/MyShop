package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    public List<Product> findProductByNameOrSku(String name, String sku);

    public Product findProductByName(String name);
    public Product findProductBySku(String sku);
    @Query("SELECT p FROM Product p JOIN p.ListProductCategories pc WHERE pc.id = :categoryId")
    List<Product> findAll(Integer categoryId);
//    @Query("SELECT p FROM Product p JOIN p.ListProductCategories pc WHERE pc.id = :categoryId and concat(p.name ,p.sku,p.ListProductCategories) like %:keyword%")
//    List<Product> findAll(Integer categoryId, String keyword);
    @Query("SELECT p FROM Product p JOIN p.ListProductCategories pc WHERE (:categoryId IS NULL OR pc.id = :categoryId) AND (:keyword IS NULL OR concat(p.name, p.sku, pc.name) LIKE %:keyword%)")
    List<Product> findAll(@Param("categoryId") Integer categoryId, @Param("keyword") String keyword);

    @Query("SELECT p FROM Product p where concat(p.name ,p.sku) like %:keyword%")
    List<Product> findAll(String keyword);
}
