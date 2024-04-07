package com.example.sm.minh.eshop.services;

import com.example.sm.minh.eshop.entities.Product;
import com.example.sm.minh.eshop.entities.ProductCategory;
import com.example.sm.minh.eshop.exceptions.ProductCategoryException;
import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.repositories.ProductCategoryRepository;
import com.example.sm.minh.eshop.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

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
    public void deleteCategory(Integer id) throws ProductCategoryException, ProductException {
        Optional<ProductCategory> categoryOptional = this.productCategoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            ProductCategory category = categoryOptional.get();
             List< Product>listProducts =this.productRepository.findAll(id,true);
             for(Product product: listProducts)
             {
                 productService.delete(product.getId());
             }
            category.setDeletedAt(new Date());
            category.setIsActive(false);
            this.productCategoryRepository.save(category);
        } else {
            throw new ProductCategoryException("Category with ID " + id + " not found");
        }
    }

    public ProductCategory findById(Integer id, Boolean isHide) throws ProductCategoryException {
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
            throw new ProductCategoryException("Category with ID " + id + " not found");
        }
    }


    public String checkNameAndSlugUnique(Integer id, String name, String slug) throws ProductCategoryException {
        // Check if the category already exists in the database
        if (id == null || id == 0) {
            ProductCategory productCategories = this.productCategoryRepository.findByNameOrSlug(name, slug);
            if (productCategories != null) {
                return "duplicated";
            }
        } else {
            ProductCategory existingCategoryByName = this.productCategoryRepository.findByName(name);
            ProductCategory existingCategoryBySlug = this.productCategoryRepository.findBySlug(slug);
            ProductCategory currentCategory = this.findById(id,false);
            // if the category exists, check if it is already in the database
            if (currentCategory == null) {
                throw new ProductCategoryException("Category with ID " + id + " not found");
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


    public void restoreCategory(Integer id) throws ProductCategoryException {
        ProductCategory categoryRestore=this.findById(id,null);
        categoryRestore.setIsActive(true);
        categoryRestore.setDeletedAt(new Date());
        this.productCategoryRepository.save(categoryRestore);
    }



    public ProductCategory setDataForProductCategory(ProductCategory productCategories, ProductCategory EditCategory)
    {
        productCategories.setName(EditCategory.getName());
        productCategories.setSlug(EditCategory.getSlug());
        productCategories.setDescription(EditCategory.getDescription());
        productCategories.setUpdatedAt(new Date());
        return productCategories;
    }
}
