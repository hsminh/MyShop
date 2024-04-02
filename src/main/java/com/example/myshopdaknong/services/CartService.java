package com.example.myshopdaknong.services;

import com.example.myshopdaknong.entity.Cart;
import com.example.myshopdaknong.entity.CartLineItem;
import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.User;
import com.example.myshopdaknong.exception.CardLineItemException;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.repository.CartLineItemRepositoty;
import com.example.myshopdaknong.repository.CartReposttory;
import com.example.myshopdaknong.repository.ProductRepository;
import com.example.myshopdaknong.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartReposttory cartReposttory;
    @Autowired
    private ProductRepository productsRepository;
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


    public User findUserById(Integer id) throws ProductException {
        return userRepository.findById(id).get();
    }


    public List<CartLineItem> getListCartItem(Cart cart) throws ProductException {
        return this.cartLineItemRepositoty.findByCartId(cart);
    }

    public Cart getCartItem(User customer) throws ProductException {
        return this.cartReposttory.findByUsersId(customer);
    }

    public String updateCartAndCartLineItem(User customer, Product selectProduct, Integer quantity) throws ProductException {
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
        CartLineItem cartLineItems=this.cartLineItemRepositoty.findByCartIdAndProductId(Cart,selectProduct);
        Cart.setCount_items(Cart.getCount_items()+quantity);

        Float taxAmount=((selectProduct.getDiscount_price()*quantity)/100)*selectProduct.getTax();
        Float PriceBeforeTax=selectProduct.getDiscount_price()*quantity;
        Cart.setTax_amount(Cart.getTax_amount()+taxAmount);
        Cart.setTotal_amount(Cart.getTotal_amount()+taxAmount+PriceBeforeTax);


            if(cartLineItems==null)
        {
            cartLineItems=new CartLineItem();
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

    public void deleteCardLineItem(Integer cardLineItemId, User customer) throws CardLineItemException {
        Optional<CartLineItem> cartLineItemsOptional = this.cartLineItemRepositoty.findById(cardLineItemId);
        if(cartLineItemsOptional.isPresent()) {
            //set Cart Null
            CartLineItem cartLineItems = cartLineItemsOptional.get();
            cartLineItems.setCartId(null);
            this.cartLineItemRepositoty.save(cartLineItems);
            //Set Cart in CartLineItem
            Cart cart=this.cartReposttory.findByUsersId(customer);
            cart.setCount_items(cart.getCount_items()-cartLineItems.getQuantity());
            cart.setTax_amount(cart.getTax_amount()-cartLineItems.getTaxTotalAmount());
            cart.setTotal_amount(cart.getTotal_amount()-cartLineItems.getTotalAmount());

            List<CartLineItem>cartLineItemContailCartId=this.cartLineItemRepositoty.findByCartId(cart);
            if (cartLineItemContailCartId.isEmpty())
            {
                cart.setDeletedAt(new Date());
            }

            this.cartReposttory.save(cart);
            this.cartLineItemRepositoty.delete(cartLineItems);
        } else {
            throw new CardLineItemException("Cannot find Cart Line Item with ID: " + cardLineItemId);
        }
    }

    public void clearCard(User Customer)
    {
        Cart cartClear=this.cartReposttory.findByUsersId(Customer);
        if(cartClear!=null)
        {
            List<CartLineItem>cartLineItemList=this.cartLineItemRepositoty.findByCartId(cartClear);
            List<CartLineItem>deleteCartLineItem=new ArrayList<>();
            for(CartLineItem cartLineItem : cartLineItemList)
            {
                cartLineItem.setCartId(null);
                deleteCartLineItem.add(cartLineItem);
            }
            this.cartLineItemRepositoty.saveAll(deleteCartLineItem);
            this.cartLineItemRepositoty.deleteAll(deleteCartLineItem);
            cartClear.setUsersId(null);
            this.cartReposttory.save(cartClear);
            this.cartReposttory.delete(cartClear);
        }
    }


}
