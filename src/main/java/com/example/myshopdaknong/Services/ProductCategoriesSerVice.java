package com.example.myshopdaknong.Services;

import com.example.myshopdaknong.Entity.Product_categories;
import com.example.myshopdaknong.Exception.Product_categoriesNotFoundException;
import com.example.myshopdaknong.Repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoriesSerVice {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    public List<Product_categories> FindAll()
    {
        return this.productCategoryRepository.findAll();
    }

    public Product_categories save(Product_categories productCategories) {
        return this.productCategoryRepository.save(productCategories);
    }

    public Product_categories DeleteCategory(Integer id) throws Product_categoriesNotFoundException {
        Optional<Product_categories> categoryOptional = this.productCategoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Product_categories category = categoryOptional.get();
            this.productCategoryRepository.delete(category);
            return category;
        } else {
            throw new Product_categoriesNotFoundException("Category with ID " + id + " not found");
        }
    }

    public Product_categories FindById(Integer id) throws Product_categoriesNotFoundException {
        Optional<Product_categories> categoryOptional = this.productCategoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Product_categories category = categoryOptional.get();
            return category;
        } else {
            throw new Product_categoriesNotFoundException("Category with ID " + id + " not found");
        }
    }

    public String CheckNameAndSlugUnique(Integer id, String name, String slug) throws Product_categoriesNotFoundException {
        if (id == null || id == 0) {
            // Trường hợp thêm mới: kiểm tra xem có bản ghi nào trùng name hoặc slug không
            Product_categories productCategories = this.productCategoryRepository.findByNameOrSlug(name, slug);
            if (productCategories != null) {
                return "duplicated";
            }
        } else {
            // Trường hợp cập nhật: kiểm tra xem có bản ghi nào trùng name hoặc slug không
            Product_categories existingCategoryByName = this.productCategoryRepository.findByName(name);
            Product_categories existingCategoryBySlug = this.productCategoryRepository.findBySlug(slug);

            // Nếu bản ghi có id đã cho không tồn tại
            Product_categories currentCategory = this.FindById(id);
            if (currentCategory == null) {
                throw new Product_categoriesNotFoundException("Category with ID " + id + " not found");
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
