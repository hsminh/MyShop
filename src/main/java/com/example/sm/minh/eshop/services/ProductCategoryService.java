package com.example.sm.minh.eshop.services;

import com.example.sm.minh.eshop.models.Product;
import com.example.sm.minh.eshop.models.ProductCategory;
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


    public ProductCategory saveCategory(ProductCategory productCategories) {
        return this.productCategoryRepository.save(productCategories);
    }
    @Transactional
    public void deleteCategory(Integer categoryId) throws ProductCategoryException, ProductException {
        Optional<ProductCategory> categoryOptional = this.productCategoryRepository.findById(categoryId);
        ProductCategory deleteCategory=categoryOptional.orElseThrow(()->new ProductCategoryException("Category with ID " + categoryId + " not found"));
        //unlink category from product
        List< Product>listProducts =this.productRepository.findAll(categoryId,true);
        for(Product product: listProducts)
        {
            productService.delete(product.getId());
        }
        deleteCategory.setDeletedAt(new Date());
        deleteCategory.setIsActive(false);
        this.productCategoryRepository.save(deleteCategory);
    }

    public ProductCategory findById(Integer id, Boolean isHide) throws ProductCategoryException {
        Optional<ProductCategory> categoryOptional=null;
        //check if category is deleted or no
        if(isHide==null)
        {
            categoryOptional = this.productCategoryRepository.findById(id);
        }else
        {
            categoryOptional = this.productCategoryRepository.findById(id,isHide);
        }

        ProductCategory targetProduct=categoryOptional.orElseThrow(()->new ProductCategoryException("Category with ID " + id + " not found"));
        return targetProduct;
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
        ProductCategory restoreCategory=this.findById(id,null);
        restoreCategory.setIsActive(true);
        restoreCategory.setDeletedAt(new Date());
        this.productCategoryRepository.save(restoreCategory);
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
