package com.example.myshopdaknong.controller.carts;

import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.Users;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.sercurity.ShopMeUserDetail;
import com.example.myshopdaknong.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartRestController {
    @Autowired
    private CartService cartService;

    @GetMapping("/cart/update-cart")
    public String updateCart(
            Model model,
            @AuthenticationPrincipal ShopMeUserDetail customer,
            @RequestParam(value = "selectProduct",required = false) Integer productId,
            @RequestParam(value = "quantity" ,required = false) int quantity) throws ProductException {
        Users Customer=this.cartService.findUserById(customer.getUserId());
        Product selectProduct=this.cartService.getProductById(productId);
        this.cartService.updateCartAndCartLineItem(Customer,selectProduct,quantity);
        model.addAttribute("messageSuccessfull","Happy");
        return "Cart updated successfully";
    }
}
