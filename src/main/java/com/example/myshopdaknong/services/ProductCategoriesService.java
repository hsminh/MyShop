package com.example.myshopdaknong.services;

import com.example.myshopdaknong.entity.ProductCategory;
import com.example.myshopdaknong.exception.ProductCategoriesException;
import com.example.myshopdaknong.repository.ProductCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoriesService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    public List<ProductCategory> findAll(String searchValue,Boolean isHide) {
        if(searchValue != null && !searchValue.trim().isEmpty()) {
            return productCategoryRepository.findByNameContaining(searchValue,isHide);
        }
        return productCategoryRepository.findAll(isHide);
    }


    public ProductCategory save(ProductCategory productCategories) {
        return this.productCategoryRepository.save(productCategories);
    }
    @Transactional
    public ProductCategory DeleteCategory(Integer id) throws ProductCategoriesException {
        Optional<ProductCategory> categoryOptional = this.productCategoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            ProductCategory category = categoryOptional.get();
            category.setDeletedAt(new Date());
            category.setIsActive(false);
            this.productCategoryRepository.save(category);
            return category;
        } else {
            throw new ProductCategoriesException("Category with ID " + id + " not found");
        }
    }

    public ProductCategory FindById(Integer id,Boolean isHide) throws ProductCategoriesException {
        System.out.println("come this1");
        Optional<ProductCategory> categoryOptional=null;
        System.out.println("come this2");

        if(isHide==null)
        {
            System.out.println("come this3");
            categoryOptional = this.productCategoryRepository.findById(id);
        }else
        {
            System.out.println("come this4");
            categoryOptional = this.productCategoryRepository.findById(id,isHide);
        }
        System.out.println("come this5");
        if (categoryOptional.isPresent()) {
            ProductCategory category = categoryOptional.get();
            return category;
        } else {
            throw new ProductCategoriesException("Category with ID " + id + " not found");
        }
    }


    public String CheckNameAndSlugUnique(Integer id, String name, String slug) throws ProductCategoriesException {
        System.out.println("come here baby");
        if (id == null || id == 0) {
            ProductCategory productCategories = this.productCategoryRepository.findByNameOrSlug(name, slug);
            if (productCategories != null) {
                return "duplicated";
            }
        } else {
            ProductCategory existingCategoryByName = this.productCategoryRepository.findByName(name);
            ProductCategory existingCategoryBySlug = this.productCategoryRepository.findBySlug(slug);
                ProductCategory currentCategory = this.FindById(id,false);
            System.out.println("existingCategoryByName "+" existingCategoryBySlug "+" currentCategory");
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


    public void restoreCategory(Integer id) throws ProductCategoriesException {
        ProductCategory categoryRestore=this.FindById(id,null);
        categoryRestore.setIsActive(true);
        categoryRestore.setDeletedAt(new Date());
        this.productCategoryRepository.save(categoryRestore);
    }
}
