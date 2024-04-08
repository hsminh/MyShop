package com.example.sm.minh.eshop.services;

import com.example.sm.minh.eshop.models.Cart;
import com.example.sm.minh.eshop.models.CartLineItem;
import com.example.sm.minh.eshop.models.Product;
import com.example.sm.minh.eshop.models.User;
import com.example.sm.minh.eshop.exceptions.CartLineItemException;
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


    public List<CartLineItem> getListCartItemByCart(Cart cart) throws ProductException {
        return this.cartLineItemRepositoty.findByCartId(cart);
    }

    public Cart getCartByCustomer(User customer) throws ProductException {
        return this.cartReposttory.findByUserId(customer);
    }

    public void addProductToCart(User customer, Product selectProduct, Integer quantity) throws ProductException {
        Cart cart = getOrCreateCart(customer);
        updateCartInfo(cart, selectProduct, quantity);
    }

    private Cart getOrCreateCart(User customer) {
        Cart cart = this.cartReposttory.findByUserId(customer);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(customer);
            cart = this.cartReposttory.save(cart);
        } else {
            cart.setUpdatedAt(new Date());
        }
        return cart;
    }

    private void updateCartInfo(Cart cart, Product selectProduct, Integer quantity) {
        CartLineItem cartLineItem = getOrCreateCartLineItem(cart, selectProduct);
        updateCartAndCartItem(cart, cartLineItem, selectProduct, quantity);
    }

    private CartLineItem getOrCreateCartLineItem(Cart cart, Product selectProduct) {
        CartLineItem cartLineItem = this.cartLineItemRepositoty.findByCartIdAndProductId(cart, selectProduct);
        if (cartLineItem == null) {
            cartLineItem = new CartLineItem();
            cartLineItem.setCreatedAt(new Date());
        } else {
            cartLineItem.setUpdatedAt(new Date());
        }
        return cartLineItem;
    }

    private void updateCartAndCartItem(Cart cart, CartLineItem cartLineItem, Product selectProduct, Integer quantity) {
        Float taxAmount = calculateTaxAmount(selectProduct, quantity);
        Float priceBeforeTax = selectProduct.getDiscountPrice() * quantity;

        cart.setCountItem(cart.getCountItem() + quantity);
        cart.setTaxAmount(cart.getTaxAmount() + taxAmount);
        cart.setTotalAmount(cart.getTotalAmount() + taxAmount + priceBeforeTax);

        cartLineItem.setQuantity(cartLineItem.getQuantity() + quantity);
        cartLineItem.setSubTotalAmount(cartLineItem.getSubTotalAmount() + priceBeforeTax);
        cartLineItem.setTaxTotalAmount(cartLineItem.getTaxTotalAmount() + taxAmount);
        cartLineItem.setTotalAmount(cartLineItem.getSubTotalAmount() + cartLineItem.getTaxTotalAmount());
        cartLineItem.setProductId(selectProduct);
        cartLineItem.setCartId(cart);

        this.cartLineItemRepositoty.save(cartLineItem);
        this.cartReposttory.save(cart);
    }

    private Float calculateTaxAmount(Product selectProduct, Integer quantity) {
        return ((selectProduct.getDiscountPrice() * quantity) / 100) * selectProduct.getTax();
    }

    public void deleteCartLineItem(Integer cardLineItemId, User customer) throws CartLineItemException {
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
            throw new CartLineItemException("Cannot find Cart Line Item with ID: " + cardLineItemId);
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


    public void checkOutCart(User customer, List<String> productIds, List<String> quantities) throws ProductException, CartLineItemException {
        int index=0;
        for(String s : productIds)
        {
            Integer quantity=Integer.parseInt(quantities.get(index++).replace(".0",""));
            this.orderService.purchaseProductDirect(Integer.parseInt(s), quantity,customer);
            this.clearCard(customer);
        }
    }
}
