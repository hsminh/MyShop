package com.example.myshopdaknong.controller;

import  com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.ProductCategory;
import com.example.myshopdaknong.exception.CategoryProductException;
import com.example.myshopdaknong.services.ProductSerVice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private ProductSerVice productSerVice;


    @GetMapping("/main-page")
    public String MainFile(@RequestParam(value = "category", required = false) Integer id,
                           @RequestParam(value = "search", required = false) String search,
                           Model model) throws CategoryProductException {
        List<ProductCategory> listCategory = productSerVice.findAllCategory();
        model.addAttribute("listCategory", listCategory);
        try {
            if (id != null) {
                ProductCategory productCategory = this.productSerVice.getCategoryById(id);
                model.addAttribute("selectCategory", productCategory);
            }

            List<Product> listProduct = productSerVice.findAll(id, search);
            model.addAttribute("search", search);
            model.addAttribute("category", id);
            model.addAttribute("listProduct", listProduct);
        } catch (CategoryProductException ex) {
            model.addAttribute("messageErr", ex.getMessage());
        }
        return "main-page";
    }




    @GetMapping("/login-form")
    public String LoginForm(Model model, @RequestParam(value = "error", required = false) String error)
    {
        if(error!=null)
        {
            model.addAttribute("messageErr","Account or password is incorrect");
        }
        return "login-form";
    }
}
