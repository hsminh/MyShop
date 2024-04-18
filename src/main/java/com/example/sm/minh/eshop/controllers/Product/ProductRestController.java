package com.example.sm.minh.eshop.controllers.Product;

import com.example.sm.minh.eshop.models.Product;
import com.example.sm.minh.eshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductRestController {
    @Autowired
    private ProductService productService;


    @GetMapping("/products/load-product")
    public List<Product> loadingProduct(@RequestParam(value = "category", required = false) Integer id,
                                        @RequestParam(value = "search", required = false) String search,
                                        @RequestParam(value = "isChoiceCategory", required = false) String isChoiceCategory ) {
        List<Product>productContain=new ArrayList<>();
        List<Product> listProduct = productService.findAll(id, search, true);
        for (Product product : listProduct)
        {
            product.setListProductCategories(null);
            productContain.add(product);
        }
        System.out.println("kajsdfoi"+productContain);
        return productContain;
    }
}
