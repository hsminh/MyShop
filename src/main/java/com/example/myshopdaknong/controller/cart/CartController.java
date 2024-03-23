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

import java.util.Date;

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

//    @PostMapping("/cart/save")
//    public String saveCart(@RequestParam("quantity")Integer quantity,
//                           @RequestParam("selectProduct")Integer productId,
//                           Model model, RedirectAttributes redirectAttributes) throws ProductException {
//        Cart newCart=new Cart();
//        Product selectProduct=this.cartService.getProductById(productId);
//        newCart.setCount_items(quantity);
//        Float taxAmount=((selectProduct.getDiscount_price()*quantity)/100)*selectProduct.getTax();
//        newCart.setTax_amount(taxAmount);
//        newCart.setTotal_amount((selectProduct.getDiscount_price()*quantity)+ newCart.getTax_amount());
//        newCart.setCreatedAt(new Date());
//        this.cartService.saveCart(newCart);
//        return "redirect:/main-page";
//    }

}
