package com.example.myshopdaknong.Services;

import com.example.myshopdaknong.Entity.Product;
import com.example.myshopdaknong.Entity.Product_categories;
import com.example.myshopdaknong.Exception.ProductException;
import com.example.myshopdaknong.Repository.ProducsRepository;
import com.example.myshopdaknong.Repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductSerVice {
    @Autowired
    private ProducsRepository producsRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    public List<Product> findAll()
    {
        return this.producsRepository.findAll();
    }

    public List<Product_categories>findAllCategory()
    {
        return this.productCategoryRepository.findAll();
    }

    public Product save(Product product) {
        product.setCreatedAt(new Date());
        return this.producsRepository.save(product);
    }

    public void delete(Integer id) throws ProductException {
        Optional<Product> productOption=this.findByid(id);
        if(productOption.isPresent())
        {
            this.producsRepository.delete(productOption.get());
        }else {
            throw new ProductException("Cannot Found Product With Id : "+id);
        }
    }

    public Optional<Product> findByid(Integer id) throws ProductException {
        Optional<Product> productOption=this.producsRepository.findById(id);
        if(productOption.isPresent())
        {
            return this.producsRepository.findById(id);
        }else {
            throw new ProductException("Cannot Found Products With Id : "+id);
        }
    }
}
