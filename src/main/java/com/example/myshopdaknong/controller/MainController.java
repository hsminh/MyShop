package com.example.myshopdaknong.controller;

import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.ProductCategory;
import com.example.myshopdaknong.service.ProductSerVice;
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
    public String MainFile(Model model)
    {
        List<Product>listProduct=productSerVice.findAll();
        List<ProductCategory>listCategory=productSerVice.findAllCategory();

        model.addAttribute("listProduct",listProduct);
        model.addAttribute("listCategory",listCategory);
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
