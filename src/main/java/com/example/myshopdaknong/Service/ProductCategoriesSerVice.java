package com.example.myshopdaknong.Service;

import com.example.myshopdaknong.Entity.Product_categories;
import com.example.myshopdaknong.Repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoriesSerVice {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    public List<Product_categories> FindAll()
    {
        return this.productCategoryRepository.findAll();
    }

}
