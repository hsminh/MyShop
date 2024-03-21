package com.example.myshopdaknong.Controllers.products;

import com.example.myshopdaknong.Entity.Product;
import com.example.myshopdaknong.Services.ProductSerVice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {
    @Autowired
    private ProductSerVice productSerVice;
    @GetMapping("/products")
    public String listProduct(Model model) {
        model.addAttribute("pageTitle","Products");
        model.addAttribute("ListProduct",this.productSerVice.findAll());
        return "products/products";
    }

    @GetMapping("/products/add")
    public String formAddProduct(Model model) {
        model.addAttribute("pageTitle","Add Product");
        model.addAttribute("Product",new Product());
        return "products/add-form-products";
    }

    @GetMapping("/products/save")
    public String saveProduts(Product Product,Model model) {
        System.out.println("cc "+ Product);
        return "products/products";
    }

}
