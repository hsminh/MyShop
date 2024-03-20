package com.example.myshopdaknong.Repository;

import com.example.myshopdaknong.Entity.Product_categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<Product_categories, Integer> {
    Product_categories findByNameOrSlug(String name, String slug);

    Product_categories findByName(String name);

    Product_categories findBySlug(String slug);
}
