package com.example.sm.minh.eshop.controllers.Order;

import com.example.sm.minh.eshop.models.User;
import com.example.sm.minh.eshop.exceptions.CartLineItemException;
import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.securities.ShopMeUserDetail;
import com.example.sm.minh.eshop.services.CartService;
import com.example.sm.minh.eshop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping("/order")
    public String saveOrder(
            @AuthenticationPrincipal ShopMeUserDetail customer,
            @RequestParam(value = "cartLineItemId",required = false) Integer cartLineItemId,
            @RequestParam(value = "quantity", required = false) Integer quantity) {
        try {
            User customerUser = this.cartService.findUserById(customer.getUserId());
            return this.orderService.purchaseFromCart(cartLineItemId, quantity, customerUser);
        } catch (ProductException | CartLineItemException ex) {
            return "Error saving order: " + ex.getMessage();
        }
    }

    @PostMapping("/order/buy-direct")
    public String buyProductDirectly(
            @AuthenticationPrincipal ShopMeUserDetail customer,
            @RequestParam(value = "productId" ,required = false) Integer productId,
            @RequestParam(value = "quantity", required = false) Integer quantity) {
        try {

            User customerUser = this.cartService.findUserById(customer.getUserId());
            String notification = this.orderService.purchaseProductDirect(productId, quantity, customerUser);
            return notification;
        } catch (ProductException | CartLineItemException ex) {
            return "Error saving order: " + ex.getMessage();
        }
    }
}
