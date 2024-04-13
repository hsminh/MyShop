package com.example.sm.minh.eshop.controllers.Cart;

import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.exceptions.UserException;
import com.example.sm.minh.eshop.models.Product;
import com.example.sm.minh.eshop.models.User;
import com.example.sm.minh.eshop.securities.ShopMeUserDetail;
import com.example.sm.minh.eshop.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CartRestController {
    @Autowired
    private CartService cartService;
    @GetMapping("/cart/update-cart")
    public ResponseEntity<Map<String, Object>> updateCart(@AuthenticationPrincipal ShopMeUserDetail customer,
                                                          @RequestParam(value = "selectProduct") Integer productId,
                                                          @RequestParam(value = "quantity") Integer quantity) {
        Map<String, Object> response = new HashMap<>();
        try {
            User customerUser = cartService.findUserById(customer.getUserId());
            if (productId == null || quantity == null || quantity <= 0) {
                throw new IllegalArgumentException("Invalid productId or quantity.");
            }
            Product selectedProduct = cartService.getProductById(productId);
            cartService.addProductToCart(customerUser, selectedProduct, quantity);
            response.put("success", true);
            response.put("message", "Shopping cart is updated Successfully");
            return ResponseEntity.ok(response);
        } catch (ProductException | IllegalArgumentException | UserException ex) {
            response.put("success", false);
            response.put("errorMessage", "Error updating cart: " + ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
