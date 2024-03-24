package com.example.myshopdaknong.controller.cart;

import com.example.myshopdaknong.entity.Cart;
import com.example.myshopdaknong.entity.CartLineItems;
import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.Users;
import com.example.myshopdaknong.exception.CardLineItemException;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.sercurity.ShopMeUserDetail;
import com.example.myshopdaknong.service.CartService;
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


    @GetMapping("/cart/remove")
    public String listCart(@RequestParam("cartLineItemId")Integer cardLineItemId) {
        this.cartService.deleteCardLineItem(cardLineItemId);
        return "cart/cart";
    }

    @GetMapping("/cart/shopping-cart")
    public String shoppingCart(@AuthenticationPrincipal ShopMeUserDetail Customer, Model model, RedirectAttributes redirectAttributes) throws ProductException {
            Users customer=this.cartService.findUserById(Customer.getUserId());
            model.addAttribute("pageTitle","Page Cart");
            Cart cartCustomer=this.cartService.getCartItem(customer);
//            model.addAttribute("cartCustomer",cartCustomer);
//            model.addAttribute("customer",customer);
        System.out.println("cc ");
        for(CartLineItems cartLineItems:  this.cartService.getListCartItem(cartCustomer))
        {
            System.out.println(cartLineItems.getProductId().loadImages());
        }
            model.addAttribute("listCartLineItem",this.cartService.getListCartItem(cartCustomer));
            return "/cart/shopping-cart";
    }

//    @GetMapping("/cart/shopping-cart")
//    public String historyShopping(@AuthenticationPrincipal ShopMeUserDetail Customer, Model model, RedirectAttributes redirectAttributes) {
//        try {
//            Users customer=this.cartService.findUserById(Customer.getUserId());
//            model.addAttribute("pageTitle","Page Cart");
//            Cart cartCustomer=this.cartService.getCartItem(customer);
//            model.addAttribute("cartCustomer",cartCustomer);
//            model.addAttribute("customer",customer);
//            model.addAttribute("listCartLineItem",this.cartService.getListCartItem(cartCustomer));
//            return "cart/shopping-cart1";
//        }catch (ProductException ex)
//        {
//            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
//            return "redirect:/main-page";
//        }
//    }
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
