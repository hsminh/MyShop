package com.example.myshopdaknong.service;

import com.example.myshopdaknong.entity.Cart;
import com.example.myshopdaknong.entity.CartLineItems;
import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.Users;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.repository.CartLineItemRepositoty;
import com.example.myshopdaknong.repository.CartReposttory;
import com.example.myshopdaknong.repository.ProductsRepository;
import com.example.myshopdaknong.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartReposttory cartReposttory;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private CartLineItemRepositoty cartLineItemRepositoty;
    @Autowired
    private UserRepository userRepository;
    public Product getProductById(Integer id) throws ProductException {
        Optional<Product> productDetail=this.productsRepository.findById(id);
        if(!productDetail.isPresent())
        {
            throw new ProductException("Cannot Found Product With Id "+id);
        }
        return productDetail.get();
    }

    public Cart saveCart(Cart cart) throws ProductException {
        return this.cartReposttory.save(cart);
    }

    public Users findUserById(Integer id) throws ProductException {
        return userRepository.findById(id).get();
    }

    public CartLineItems saveCartLineItem(CartLineItems cartLineItems) throws ProductException {
        return this.cartLineItemRepositoty.save(cartLineItems);
    }

    public String updateCartAndCartLineItem(Users customer, Product selectProduct, Integer quantity) throws ProductException {
        //luu cart
        Cart Cart=this.cartReposttory.findByUsersId(customer);
        if(Cart==null)
        {
            Cart=new Cart();
            Cart.setUsersId(customer);
            Cart=this.cartReposttory.save(Cart);
        }else
        {
            Cart.setUpdatedAt(new Date());
        }
        CartLineItems cartLineItemContainProduct=this.cartLineItemRepositoty.findByCartId_Id(Cart.getId());
        if(cartLineItemContainProduct==null)
        {
            Cart.setCount_items(Cart.getCount_items()+1);
        }
        Float taxAmount=((selectProduct.getDiscount_price()*quantity)/100)*selectProduct.getTax();
        Float PriceBeforeTax=selectProduct.getDiscount_price()*quantity;
        Cart.setTax_amount(Cart.getTax_amount()+taxAmount);
        Cart.setTotal_amount(Cart.getTotal_amount()+taxAmount+PriceBeforeTax);


        //luu cart line item
        CartLineItems cartLineItems=this.cartLineItemRepositoty.findByProductId(selectProduct);
        if(cartLineItems==null)
        {
            cartLineItems=new CartLineItems();
            cartLineItems.setCreatedAt(new Date());
        }else
        {
            cartLineItems.setUpdatedAt(new Date());
        }
        cartLineItems.setQuantity(cartLineItems.getQuantity()+quantity);
        cartLineItems.setSubTotalAmount(cartLineItems.getSubTotalAmount() + PriceBeforeTax);
        cartLineItems.setTaxTotalAmount(cartLineItems.getTaxTotalAmount() + taxAmount);
        cartLineItems.setTotalAmount(cartLineItems.getSubTotalAmount() + cartLineItems.getTaxTotalAmount());
        cartLineItems.setProductId(selectProduct);
        cartLineItems.setCartId(Cart);
        this.cartLineItemRepositoty.save(cartLineItems);
        this.cartReposttory.save(Cart);
        return "ok";
    }


}
