package com.example.myshopdaknong.Controllers.productCategory;

import com.example.myshopdaknong.Exception.Product_categoriesNotFoundException;
import com.example.myshopdaknong.Services.ProductCategoriesSerVice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {
    @Autowired
    private ProductCategoriesSerVice productCategoriesSerVice;
    @GetMapping("/category/check-slug-name-uni")
    public String CheckSlugAndNameUni(@RequestParam("name")String name,@RequestParam("slug")String slug,@RequestParam("id")Integer id) throws Product_categoriesNotFoundException {
        return this.productCategoriesSerVice.CheckNameAndSlugUnique(id,name,slug);
    }
}
