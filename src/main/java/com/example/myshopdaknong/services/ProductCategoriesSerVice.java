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
public class ProductCategoriesSerVice {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    public List<ProductCategory> FindAll()
    {
        return this.productCategoryRepository.findAll();
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
                System.out.println("come he");
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
            // Trường hợp thêm mới: kiểm tra xem có bản ghi nào trùng name hoặc slug không
            ProductCategory productCategories = this.productCategoryRepository.findByNameOrSlug(name, slug);
            if (productCategories != null) {
                return "duplicated";
            }
        } else {
            // Trường hợp cập nhật: kiểm tra xem có bản ghi nào trùng name hoặc slug không
            ProductCategory existingCategoryByName = this.productCategoryRepository.findByName(name);
            ProductCategory existingCategoryBySlug = this.productCategoryRepository.findBySlug(slug);

            // Nếu bản ghi có id đã cho không tồn tại
            ProductCategory currentCategory = this.FindById(id);
            if (currentCategory == null) {
                throw new ProductCategoriesException("Category with ID " + id + " not found");
            }

            // Kiểm tra trùng name
            if (existingCategoryByName != null && !existingCategoryByName.getId().equals(id)) {
                return "duplicated";
            }

            // Kiểm tra trùng slug
            if (existingCategoryBySlug != null && !existingCategoryBySlug.getId().equals(id)) {
                return "duplicated";
            }
        }

        return "ok";
    }


}
