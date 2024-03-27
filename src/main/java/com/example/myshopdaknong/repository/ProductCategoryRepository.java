package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    ProductCategory findByNameOrSlug(String name, String slug);

    ProductCategory findByName(String name);

    ProductCategory findBySlug(String slug);


    List<ProductCategory> findByNameContaining(String searchValue);
}
