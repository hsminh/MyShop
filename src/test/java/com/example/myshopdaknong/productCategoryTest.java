package com.example.myshopdaknong;

import com.example.myshopdaknong.Entity.Product_categories;
import com.example.myshopdaknong.Repository.ProductCategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class productCategoryTest {
    @Autowired
    ProductCategoryRepository productCategoryRepository;



    @Test
    public void CreateProducCategory()
    {
        ArrayList<Product_categories>list=new ArrayList<>();
        Product_categories Electronics=new Product_categories("Electronics","electronics","This category includes all electronic devices such as smartphones, laptops, and cameras.");
        Product_categories Clothing=new Product_categories("Clothing","clothing","Find a wide range of clothing items here, from t-shirts and jeans to dresses and jackets.");
        Product_categories Books=new Product_categories("Books","books","Explore our collection of books covering various genres from fiction to non-fiction");
        Product_categories Home12	=new Product_categories("\tHome & Garden","home-garden","Everything you need for your home and garden, including furniture, decor, and tools..");
        Product_categories Sports	=new Product_categories("Sports\t","sports","\tGet your sports gear here, from equipment for various sports to activewear and accessories");
        list.addAll(List.of(Electronics,Clothing,Books,Home12,Sports));
        this.productCategoryRepository.saveAll(this.productCategoryRepository.saveAll(list));
    }
}
