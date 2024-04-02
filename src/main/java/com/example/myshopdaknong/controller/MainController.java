package com.example.myshopdaknong.controller;

import  com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.ProductCategory;
import com.example.myshopdaknong.entity.ProductDTO;
import com.example.myshopdaknong.exception.CategoryProductException;
import com.example.myshopdaknong.services.ProductService;
import com.example.myshopdaknong.util.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                productDTOS=productSerVice.productOrderMost1();
            }

            List<Product> listProduct = productSerVice.findAll(id, search);
            model.addAttribute("search", search);
            model.addAttribute("category", id);
            model.addAttribute("listProduct", listProduct);
            model.addAttribute("isChoice", "Shop");
            model.addAttribute("isChoiceCategory", isChoiceCategory);
            model.addAttribute("productsOrderedMost", productDTOS);
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
