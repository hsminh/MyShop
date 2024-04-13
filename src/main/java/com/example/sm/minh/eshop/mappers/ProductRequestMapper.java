package com.example.sm.minh.eshop.mappers;

import com.example.sm.minh.eshop.models.Product;
import com.example.sm.minh.eshop.requests.ProductRequest;

public class ProductRequestMapper {
    public static Product toProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setId(productRequest.getId());
        product.setIsActive(productRequest.getIsActive());
        product.setContent(productRequest.getContent());
        product.setSku(productRequest.getSku());
        product.setName(productRequest.getName());
        product.setTax(productRequest.getTax());
        product.setPrice(productRequest.getPrice());
        product.setDiscountPrice(productRequest.getDiscountPrice());
        product.setListProductCategories(productRequest.getListProductCategories());
        return product;
    }

    public static ProductRequest toProductRequest(Product product) {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setId(product.getId());
        productRequest.setIsActive(product.getIsActive());
        productRequest.setContent(product.getContent());
        productRequest.setSku(product.getSku());
        productRequest.setName(product.getName());
        productRequest.setTax(product.getTax());
        productRequest.setPrice(product.getPrice());
        productRequest.setDiscountPrice(product.getDiscountPrice());
        productRequest.setListProductCategories(product.getListProductCategories());
        return productRequest;
    }
}
