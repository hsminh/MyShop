package com.example.myshopdaknong.services;

import com.example.myshopdaknong.entity.Cart;
import com.example.myshopdaknong.entity.CartLineItem;
import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.ProductCategory;
import com.example.myshopdaknong.exception.CategoryProductException;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private static final Integer INT_PAGE_SIZE=5;
    @Autowired
    private ProductRepository producsRepository;
    @Autowired
    private CartLineItemRepositoty cartLineItemRepositoty;

    @Autowired
    private CartReposttory cartReposttory;
    @Autowired
    private OrderLineItemRepository orderLineItemRepository;
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

    public List<ProductCategory>findAllCategoryContainProduct()
    {
        return this.productCategoryRepository.findAllCategoriesWithProducts();
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
            Product product=productOption.get();
            for(CartLineItem cartLineItem : this.cartLineItemRepositoty.findByProductId(product))
            {
                Cart setCart=cartLineItem.getCartId();
                setCart.setTax_amount(setCart.getTax_amount()-cartLineItem.getTaxTotalAmount());
                setCart.setCount_items(setCart.getCount_items()-cartLineItem.getQuantity());
                setCart.setTotal_amount(setCart.getTotal_amount()-cartLineItem.getTotalAmount());
                setCart.setCreatedAt(new Date());
                cartLineItem.setCartId(null);
                this.cartLineItemRepositoty.save(cartLineItem);
                this.cartReposttory.save(setCart);
                this.cartLineItemRepositoty.delete(cartLineItem);
            }
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

    public List<Product> productOrderMost() {
        Pageable pageable = PageRequest.of(0, INT_PAGE_SIZE);
        List<Product> listProductOrderMost = this.orderLineItemRepository.findProductsOrderedMost(pageable);
        return listProductOrderMost;
    }

}
