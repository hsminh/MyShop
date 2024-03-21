package com.example.myshopdaknong.Services;

import com.example.myshopdaknong.Entity.Product;
import com.example.myshopdaknong.Repository.ProducsRepository;
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
