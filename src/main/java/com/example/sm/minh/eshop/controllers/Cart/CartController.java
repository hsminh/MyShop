package com.example.sm.minh.eshop.controllers.Cart;

import com.example.sm.minh.eshop.entities.Cart;
import com.example.sm.minh.eshop.entities.Product;
import com.example.sm.minh.eshop.entities.User;
import com.example.sm.minh.eshop.exceptions.CardLineItemException;
import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.securities.ShopMeUserDetail;
import com.example.sm.minh.eshop.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public String showCart(@RequestParam("productId") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product chosenProduct = this.cartService.getProductById(id);
            model.addAttribute("pageTitle", "Page Cart");
            model.addAttribute("cart", new Cart());
            model.addAttribute("cartProduct", chosenProduct);
            return "cart/cart";
        } catch (ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/main-page";
        }
    }


    @GetMapping("/cart/remove")
    public String removeCartItem(@AuthenticationPrincipal ShopMeUserDetail customer,
                                     @RequestParam("cartLineItemId") Integer cartLineItemId,
                                     RedirectAttributes redirectAttributes) {
        try {
            User user = this.cartService.findUserById(customer.getUserId());
            this.cartService.deleteCardLineItem(cartLineItemId, user);
        } catch (CardLineItemException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        } catch (ProductException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/cart/shopping-cart";
    }

    @GetMapping("/cart/shopping-cart")
    public String showShoppingCart(@AuthenticationPrincipal ShopMeUserDetail customer,
                                   Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = this.cartService.findUserById(customer.getUserId());
            model.addAttribute("pageTitle", "Page Cart");
            Cart cartCustomer = this.cartService.getCartItem(user);
            model.addAttribute("Cart", cartCustomer);
            model.addAttribute("listCartLineItem", this.cartService.getListCartItem(cartCustomer));
            return "/cart/shopping-cart";
        } catch (ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/main-page";
        }
    }
    @PostMapping("/cart/checkout")
    public String checkOutAll(@AuthenticationPrincipal ShopMeUserDetail customer,
                              @RequestParam("productIds") List<String> productIds,
                              @RequestParam("quantities") List<String> quantities,
                              RedirectAttributes redirectAttributes) throws ProductException {
        try {
            User user = this.cartService.findUserById(customer.getUserId());
            this.cartService.checkOutAll(user,productIds,quantities);
            redirectAttributes.addFlashAttribute("Message","Congratulation! You're Buy Successfully");
            return "redirect:/main-page";
        } catch (ProductException ex) {
            return "redirect:/main-page";
        } catch (CardLineItemException e) {
            throw new RuntimeException(e);
        }
    }


}
