package com.example.myshopdaknong.controller.cart;

import com.example.myshopdaknong.entity.Cart;
import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.ProductCategory;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;
    @GetMapping("/cart")
    public String listCart(@RequestParam("productId")Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product chooseProduct=this.cartService.getProductById(id);
            model.addAttribute("pageTitle","Page Cart");
            model.addAttribute("cart",new Cart());
            model.addAttribute("cartProduct",chooseProduct);
            return "cart/cart";
        }catch (ProductException ex)
        {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/main-page";
        }

    }

    @PostMapping("/cart/save")
    public String saveCart(Cart cart,Model model, RedirectAttributes redirectAttributes) {
        System.out.println("cc "+cart );
        return "redirect:/main-page";
    }

}
