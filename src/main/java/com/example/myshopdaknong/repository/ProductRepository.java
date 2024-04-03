package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query("select p from Product p where p.id =:id and p.isActive=true")
    public Optional<Product>findById(Integer id);
    @Query("select p from Product  p where p.isActive=false and p.id=:id")
    public Optional<Product>findProductByIsActiveIsFalse(Integer id);
    public List<Product> findProductByNameOrSku(String name, String sku);

    public Product findProductByName(String name);
    public Product findProductBySku(String sku);
    @Query("SELECT p FROM Product p JOIN p.ListProductCategories pc WHERE pc.id = :categoryId and p.isActive=:isHide")
    List<Product> findAll(Integer categoryId,@Param("isHide") Boolean isHide);

    @Query("SELECT p FROM Product p JOIN p.ListProductCategories pc WHERE (:categoryId IS NULL OR pc.id = :categoryId) AND (:keyword IS NULL OR concat(p.name, p.sku, pc.name) LIKE %:keyword%) and p.isActive= :isHide")
    List<Product> findAll(@Param("categoryId") Integer categoryId, @Param("keyword") String keyword,@Param("isHide") Boolean isHide);

    @Query("SELECT p FROM Product p where concat(p.name ,p.sku) like %:keyword% and p.isActive=:isHide")
    List<Product> findAll(String keyword,@Param("isHide") Boolean isHide);

    @Query("SELECT p FROM Product p where p.isActive=:isHide")
    List<Product> findAll(@Param("isHide") Boolean isHide);


}
