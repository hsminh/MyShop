package com.example.myshopdaknong.services;

import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.ProductCategory;
import com.example.myshopdaknong.exception.CategoryProductException;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.repository.ProductRepository;
import com.example.myshopdaknong.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository producsRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    public List<Product> findAll(Integer id, String search)
    {
        if(id!=null&&search!=null)
        {
            return this.producsRepository.findAll(id,search);
        }else if(id!=null)
        {
            return this.producsRepository.findAll(id);
        }else if(search!=null&&!search.trim().isEmpty())
        {
            return this.producsRepository.findAll(search);

        }
        return this.producsRepository.findAll();
    }

    public List<ProductCategory>findAllCategory()
    {
        return this.productCategoryRepository.findAll();
    }

    public ProductCategory getCategoryById(Integer id) throws CategoryProductException {
        Optional<ProductCategory> productCategory= this.productCategoryRepository.findById(id);
        if(productCategory.isPresent())
        {
            return  productCategory.get();
        }
        throw new CategoryProductException("Cannot Find Category With ID :"+id);
    }

    public Product save(Product product) {
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

    public String checkNameAndSkuUnique(String name, String sku, Integer id) {
        if (id == null || id == 0) {
            List<Product> checkProductsByNameOrSku = producsRepository.findProductByNameOrSku(name, sku);
            if (!checkProductsByNameOrSku.isEmpty()) {
                return "duplicated";
            }
        } else {
            Product existingProduct = producsRepository.findById(id).orElse(null);
            if (existingProduct == null) {
                // Handle case where product with given id does not exist
                return "not_found";
            }

            Product checkProductByName = producsRepository.findProductByName(name);
            Product checkProductBySku = producsRepository.findProductBySku(sku);

            // Check if the new name or sku conflicts with existing products
            if ((checkProductBySku != null && !existingProduct.getSku().equals(sku)) ||
                    (checkProductByName != null && !existingProduct.getName().equals(name))) {
                return "duplicated";
            }
        }
        return "ok";
    }
}
