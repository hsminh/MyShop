package com.example.myshopdaknong.Controller.Users;

import com.example.myshopdaknong.Service.ProductCategoriesSerVice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductCategoryController {
    @Autowired
    private ProductCategoriesSerVice productCategoriesSerVice;
    @GetMapping("/category")
    public String ListProduct(Model model) {
        model.addAttribute("ListCate",this.productCategoriesSerVice.FindAll());
        return "category/category";
    }
}
