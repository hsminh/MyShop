package com.example.myshopdaknong.Service;

import com.example.myshopdaknong.Entity.Product;
import com.example.myshopdaknong.Entity.Product_categories;
import com.example.myshopdaknong.Repository.ProducsRepository;
import com.example.myshopdaknong.Repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSerVice {
    @Autowired
    private ProducsRepository producsRepository;
    public List<Product> findAll()
    {
        return this.producsRepository.findAll();
    }
}
