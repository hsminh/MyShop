package com.example.myshopdaknong.services;

import com.example.myshopdaknong.entity.ProductCategory;
import com.example.myshopdaknong.exception.ProductCategoriesException;
import com.example.myshopdaknong.repository.ProductCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoriesService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    public List<ProductCategory> findAll(String searchValue) {
        if(searchValue != null && !searchValue.trim().isEmpty()) {
            return productCategoryRepository.findByNameContaining(searchValue);
        }
        return productCategoryRepository.findAll();
    }


    public ProductCategory save(ProductCategory productCategories) {
        return this.productCategoryRepository.save(productCategories);
    }
    @Transactional
    public ProductCategory DeleteCategory(Integer id) throws ProductCategoriesException {
        Optional<ProductCategory> categoryOptional = this.productCategoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            ProductCategory category = categoryOptional.get();
            if(category.getLiProducts().size()!=0)
            {
                    throw new ProductCategoriesException("Category with ID " + id + "contains products, cannot delete.");
            }
            this.productCategoryRepository.delete(category);
            return category;
        } else {
            throw new ProductCategoriesException("Category with ID " + id + " not found");
        }
    }

    public ProductCategory FindById(Integer id) throws ProductCategoriesException {
        Optional<ProductCategory> categoryOptional = this.productCategoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            ProductCategory category = categoryOptional.get();
            return category;
        } else {
            throw new ProductCategoriesException("Category with ID " + id + " not found");
        }
    }

    public String CheckNameAndSlugUnique(Integer id, String name, String slug) throws ProductCategoriesException {
        if (id == null || id == 0) {
            // Check
            ProductCategory productCategories = this.productCategoryRepository.findByNameOrSlug(name, slug);
            if (productCategories != null) {
                return "duplicated";
            }
        } else {
            ProductCategory existingCategoryByName = this.productCategoryRepository.findByName(name);
            ProductCategory existingCategoryBySlug = this.productCategoryRepository.findBySlug(slug);

            ProductCategory currentCategory = this.FindById(id);
            if (currentCategory == null) {
                throw new ProductCategoriesException("Category with ID " + id + " not found");
            }

            if (existingCategoryByName != null && !existingCategoryByName.getId().equals(id)) {
                return "duplicated";
            }

            if (existingCategoryBySlug != null && !existingCategoryBySlug.getId().equals(id)) {
                return "duplicated";
            }
        }

        return "ok";
    }


}
