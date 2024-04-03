package com.example.myshopdaknong.controller.productcategory;

import com.example.myshopdaknong.exception.ProductCategoriesException;
import com.example.myshopdaknong.services.ProductCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    public class ProductCategoryRestController {
        @Autowired
        private ProductCategoriesService productCategoriesSerVice;
        @GetMapping("/category/check-slug-name-uni")
        public String CheckSlugAndNameUni(@RequestParam("name")String name,@RequestParam("slug")String slug,@RequestParam(value = "id" ,required = false)Integer id) throws ProductCategoriesException {
            return this.productCategoriesSerVice.CheckNameAndSlugUnique(id,name,slug);
        }
    }
