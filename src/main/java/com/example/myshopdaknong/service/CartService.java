package com.example.myshopdaknong.service;

import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.repository.CartReposttory;
import com.example.myshopdaknong.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartReposttory cartReposttory;
    @Autowired
    private ProductsRepository productsRepository;

    public Product getProductById(Integer id) throws ProductException {
        Optional<Product> productDetail=this.productsRepository.findById(id);
        if(!productDetail.isPresent())
        {
            throw new ProductException("Cannot Found Product With Id "+id);
        }
        return productDetail.get();
    }
}
