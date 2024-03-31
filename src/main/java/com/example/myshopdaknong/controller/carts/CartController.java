package com.example.myshopdaknong.controller.carts;

import com.example.myshopdaknong.entity.Cart;
import com.example.myshopdaknong.entity.CartLineItem;
import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.User;
import com.example.myshopdaknong.exception.CardLineItemException;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.sercurity.ShopMeUserDetail;
import com.example.myshopdaknong.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String removeCartLineItem(@AuthenticationPrincipal ShopMeUserDetail customer,
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
            for (CartLineItem cartLineItems : this.cartService.getListCartItem(cartCustomer)) {
                System.out.println(cartLineItems.getProductId().loadImages());
            }
            model.addAttribute("listCartLineItem", this.cartService.getListCartItem(cartCustomer));
            return "/cart/shopping-cart";
        } catch (ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/main-page";
        }
    }


}
