package com.example.myshopdaknong.controller.Orders;

import com.example.myshopdaknong.entity.User;
import com.example.myshopdaknong.exception.CardLineItemException;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.sercurity.ShopMeUserDetail;
import com.example.myshopdaknong.services.CartService;
import com.example.myshopdaknong.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
            String notification = this.orderService.saveOrder(cartLineItemId, quantity, customerUser);
            return notification;
        } catch (ProductException | CardLineItemException ex) {
            return "Error saving order: " + ex.getMessage();
        }
    }

    @GetMapping("/order/buy-direct")
    public String buyDirect(
            @AuthenticationPrincipal ShopMeUserDetail customer,
            @RequestParam(value = "productId" ,required = false) Integer productId,
            @RequestParam(value = "quantity", required = false) Integer quantity) {
        try {

            User customerUser = this.cartService.findUserById(customer.getUserId());
            String notification = this.orderService.saveOrderDirect(productId, quantity, customerUser);
            return notification;
        } catch (ProductException | CardLineItemException ex) {
            return "Error saving order: " + ex.getMessage();
        }
    }
}
