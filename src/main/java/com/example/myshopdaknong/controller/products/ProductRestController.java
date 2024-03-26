package com.example.myshopdaknong.controller.products;

import com.example.myshopdaknong.services.ProductSerVice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {
    @Autowired
    private ProductSerVice productSerVice;

    @GetMapping("/products/check-sku-name-unique")
    public String CheckSkuAndNameUni(@RequestParam("name") String name, @RequestParam("sku") String sku, @RequestParam(value = "id", required = false) Integer id) {
        System.out.println("cc dm "+this.productSerVice.checkNameAndSkuUnique(name, sku, id));
        return this.productSerVice.checkNameAndSkuUnique(name, sku, id);
    }
}
