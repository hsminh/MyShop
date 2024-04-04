package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    ProductCategory findByNameOrSlug(String name, String slug);

    ProductCategory findByName(String name);
    ProductCategory findBySlug(String slug);

    @Query("SELECT p from product_categories p where p.id = :id and p.isActive=:isHide")
    Optional<ProductCategory> findById(Integer id,Boolean isHide);

//    List<ProductCategory> findAllByLiProductsIsNotNull();
//    List<ProductCategory> findByLiProductsIsNotNull();
@Query("SELECT DISTINCT pc FROM product_categories pc JOIN FETCH pc.LiProducts p WHERE p.isActive = true")
List<ProductCategory> findAllCategoriesWithProducts();
    @Query("SELECT pc FROM product_categories pc WHERE pc.name LIKE %:searchValue% and pc.isActive=:isHide ")
    List<ProductCategory> findByNameContaining(String searchValue,Boolean isHide);
    @Query("SELECT pc FROM product_categories pc WHERE pc.isActive=:isHide ")
    List<ProductCategory> findAll(Boolean isHide);

}
