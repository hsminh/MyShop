package com.example.sm.minh.eshop.controllers.Cart;

import com.example.sm.minh.eshop.entities.Product;
import com.example.sm.minh.eshop.entities.User;
import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.securities.ShopMeUserDetail;
import com.example.sm.minh.eshop.services.CartService;
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
            @RequestParam(value = "selectProduct", required = false) Integer productId,
            @RequestParam(value = "quantity", required = false) Integer quantity) {
        try {
            User customerUser = this.cartService.findUserById(customer.getUserId());
            if (productId == null || quantity == null || quantity <= 0) {
                throw new IllegalArgumentException("Invalid productId or quantity.");
            }
            Product selectedProduct = this.cartService.getProductById(productId);
            this.cartService.addProductToCart(customerUser, selectedProduct, quantity);
            model.addAttribute("messageSuccess", "Cart updated successfully");
            return "Cart updated successfully";
        } catch (ProductException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "Error updating cart: " + ex.getMessage();
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "Error updating cart: " + ex.getMessage();
        }
    }

    @GetMapping("/cart/clear")
    public void clearAllCardAndCartLineItem(@AuthenticationPrincipal ShopMeUserDetail customer) throws ProductException {
        User customerUser = this.cartService.findUserById(customer.getUserId());
        this.cartService.clearCard(customerUser);
    }
}
