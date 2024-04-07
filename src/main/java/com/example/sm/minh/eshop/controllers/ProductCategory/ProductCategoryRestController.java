package com.example.sm.minh.eshop.controllers.ProductCategory;

import com.example.sm.minh.eshop.exceptions.ProductCategoryException;
import com.example.sm.minh.eshop.services.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    public class ProductCategoryRestController {
        @Autowired
        private ProductCategoryService productCategoriesSerVice;
        @GetMapping("/category/check-slug-name-uni")
        public String CheckSlugAndNameUni(@RequestParam("name")String name,@RequestParam("slug")String slug,@RequestParam(value = "id" ,required = false)Integer id) throws ProductCategoryException {
            return this.productCategoriesSerVice.checkNameAndSlugUnique(id,name,slug);
        }
    }
