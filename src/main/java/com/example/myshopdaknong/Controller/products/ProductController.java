package com.example.myshopdaknong.Controller.products;

import com.example.myshopdaknong.Service.ProductSerVice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {
    @Autowired
    private ProductSerVice productSerVice;
    @GetMapping("/products")
    public String ListProduct(Model model) {
        model.addAttribute("pageTitle","Products");
        model.addAttribute("ListProduct",this.productSerVice.findAll());
        return "products/products";
    }
}
