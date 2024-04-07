package com.example.sm.minh.eshop.services;

import com.example.sm.minh.eshop.entities.Cart;
import com.example.sm.minh.eshop.entities.CartLineItem;
import com.example.sm.minh.eshop.entities.Product;
import com.example.sm.minh.eshop.entities.User;
import com.example.sm.minh.eshop.exceptions.CardLineItemException;
import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.repositories.CartLineItemRepositoty;
import com.example.sm.minh.eshop.repositories.CartReposttory;
import com.example.sm.minh.eshop.repositories.ProductRepository;
import com.example.sm.minh.eshop.repositories.UserRepository;
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
    @Autowired
    private OrderService orderService;
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
        return this.cartReposttory.findByUserId(customer);
    }

    public String updateCartAndCartLineItem(User customer, Product selectProduct, Integer quantity) throws ProductException {
        Cart Cart=this.cartReposttory.findByUserId(customer);
        if(Cart==null)
        {
            Cart=new Cart();
            Cart.setUserId(customer);
            Cart=this.cartReposttory.save(Cart);
        }else
        {
            Cart.setUpdatedAt(new Date());
        }
        CartLineItem cartLineItems=this.cartLineItemRepositoty.findByCartIdAndProductId(Cart,selectProduct);
        Cart.setCountItem(Cart.getCountItem()+quantity);

        Float taxAmount=((selectProduct.getDiscountPrice()*quantity)/100)*selectProduct.getTax();
        Float PriceBeforeTax=selectProduct.getDiscountPrice()*quantity;
        Cart.setTaxAmount(Cart.getTaxAmount()+taxAmount);
        Cart.setTotalAmount(Cart.getTotalAmount()+taxAmount+PriceBeforeTax);


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
        System.out.println("DCM "+cartLineItems);
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
            Cart cart=this.cartReposttory.findByUserId(customer);
            cart.setCountItem(cart.getCountItem()-cartLineItems.getQuantity());
            cart.setTaxAmount(cart.getTaxAmount()-cartLineItems.getTaxTotalAmount());
            cart.setTotalAmount(cart.getTotalAmount()-cartLineItems.getTotalAmount());

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
        Cart cartClear=this.cartReposttory.findByUserId(Customer);
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
            cartClear.setUserId(null);
            this.cartReposttory.save(cartClear);
            this.cartReposttory.delete(cartClear);
        }
    }


    public void checkOutAll(User customer, List<String> productIds, List<String> quantities) throws ProductException, CardLineItemException {
        int index=0;
        for(String s : productIds)
        {
            Integer quantity=Integer.parseInt(quantities.get(index++).replace(".0",""));
            this.orderService.saveOrderDirect(Integer.parseInt(s), quantity,customer);
            this.clearCard(customer);
        }
    }
}
