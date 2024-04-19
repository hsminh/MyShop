package com.example.sm.minh.eshop.controllers.Product;

import com.example.sm.minh.eshop.dto.ProductDTO;
import com.example.sm.minh.eshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductRestController {
    @Autowired
    private ProductService productService;


    @GetMapping("/products/load-product")
    public List<ProductDTO> loadingProduct(@RequestParam(value = "category", required = false) Integer categoryId,
                                        @RequestParam(value = "search", required = false) String search) {
        List<ProductDTO>productContain=new ArrayList<>();
        if(search.isEmpty()||search.trim().isEmpty()) search=null;
        List<ProductDTO> listProduct = productService.findAll(categoryId, search, true);
        for (ProductDTO product : listProduct)
        {
            System.out.println("dcmm"+product.getProduct().getListProductCategories());
            product.getProduct().setListProductCategories(null);
            productContain.add(product);
        }
        return productContain;
    }
}
