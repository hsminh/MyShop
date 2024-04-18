package com.example.sm.minh.eshop.controllers;

import com.example.sm.minh.eshop.dto.ProductDTO;
import com.example.sm.minh.eshop.models.Product;
import com.example.sm.minh.eshop.models.ProductCategory;
import com.example.sm.minh.eshop.exceptions.CategoryProductException;
import com.example.sm.minh.eshop.services.ProductService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private ProductService productSerVice;

    @GetMapping("/main-page")
    public String MainFile(@RequestParam(value = "category", required = false) Integer id,
                           @RequestParam(value = "search", required = false) String search,
                           @RequestParam(value = "isChoiceCategory", required = false) String isChoiceCategory,
                           Model model) throws CategoryProductException {
        List<ProductCategory> listCategory = productSerVice.findAllCategoryContainProduct();
        model.addAttribute("listCategory", listCategory);
        try {
            if (id != null) {
                ProductCategory productCategory = this.productSerVice.getCategoryById(id);
                model.addAttribute("selectCategory", productCategory);
            }
            ArrayList<ProductDTO>productDTOS=null;
            if(id==null&&search==null)
            {
                productDTOS=productSerVice.productOrderMost();
            }

            List<Product> listProduct = productSerVice.findAll(id, search,true);
            model.addAttribute("search", search);
            model.addAttribute("pageTitle","SDN Shop");

            model.addAttribute("category", id);
            model.addAttribute("listProduct", listProduct);
            model.addAttribute("isChoice", "Shop");
            model.addAttribute("isChoiceCategory", isChoiceCategory);
            model.addAttribute("productsOrderedMost", productDTOS);
        } catch (CategoryProductException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        return "main-page";
    }


    @GetMapping("/login-form")
    public String LoginForm(Model model, @RequestParam(value = "error", required = false) String error)
    {
        if(error!=null)
        {
            model.addAttribute("errorMessage","UserName or password is incorrect");
        }
        model.addAttribute("pageTitle","Login");
        return "login-form";
    }
}