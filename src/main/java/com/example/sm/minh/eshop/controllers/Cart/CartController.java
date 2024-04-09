    package com.example.sm.minh.eshop.controllers.Cart;

    import com.example.sm.minh.eshop.models.Cart;
    import com.example.sm.minh.eshop.models.Product;
    import com.example.sm.minh.eshop.models.User;
    import com.example.sm.minh.eshop.exceptions.CartLineItemException;
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
        public String viewCart(@RequestParam("productId") Integer productId, Model model, RedirectAttributes redirectAttributes) {
            try {
                Product cartProduct = this.cartService.getProductById(productId);
                model.addAttribute("pageTitle", "Cart ID |"+productId);
                model.addAttribute("cartProduct", cartProduct);
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
                this.cartService.deleteCartLineItem(cartLineItemId, user);
            } catch (CartLineItemException ex) {
                redirectAttributes.addFlashAttribute("message", ex.getMessage());
            } catch (ProductException e) {
                throw new RuntimeException(e);
            }
            return "redirect:/cart/shopping-cart";
        }

        @GetMapping("/cart/shopping-cart")
        public String viewShoppingCart(@AuthenticationPrincipal ShopMeUserDetail customer,
                                       Model model, RedirectAttributes redirectAttributes) {
            try {
                User user = this.cartService.findUserById(customer.getUserId());
                model.addAttribute("pageTitle", "Cart");
                Cart shoppingCart = this.cartService.getCartByCustomer(user);
                model.addAttribute("shoppingCart", shoppingCart);
                model.addAttribute("listCartLineItem", this.cartService.getListCartItemByCart(shoppingCart));
                return "/cart/shopping-cart";
            } catch (ProductException ex) {
                redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
                return "redirect:/main-page";
            }
        }
        @PostMapping("/cart/checkout")
        public String checkOutCart(@AuthenticationPrincipal ShopMeUserDetail customer,
                                  @RequestParam("productIds") List<String> productIds,
                                  @RequestParam("quantities") List<String> quantities,
                                  RedirectAttributes redirectAttributes) throws ProductException {
            try {
                User user = this.cartService.findUserById(customer.getUserId());
                this.cartService.checkOutCart(user,productIds,quantities);
                redirectAttributes.addFlashAttribute("Message","Congratulation! You're Buy Successfully");
                return "redirect:/main-page";
            } catch (ProductException ex) {
                return "redirect:/main-page";
            } catch (CartLineItemException e) {
                throw new RuntimeException(e);
            }
        }


    }
