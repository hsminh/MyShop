    package com.example.sm.minh.eshop.controllers.Cart;

    import com.example.sm.minh.eshop.exceptions.CartExeption;
    import com.example.sm.minh.eshop.exceptions.UserException;
    import com.example.sm.minh.eshop.models.Cart;
    import com.example.sm.minh.eshop.models.Product;
    import com.example.sm.minh.eshop.models.User;
    import com.example.sm.minh.eshop.exceptions.CartLineItemException;
    import com.example.sm.minh.eshop.exceptions.ProductException;
    import com.example.sm.minh.eshop.securities.ShopMeUserDetail;
    import com.example.sm.minh.eshop.services.CartService;
    import com.example.sm.minh.eshop.services.OrderService;
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
        @Autowired
        private OrderService orderService;

        @GetMapping("/cart")
        public String viewCart(@RequestParam("productId") Integer productId, Model model, RedirectAttributes redirectAttributes) {
            try {
                Product cartProduct = this.cartService.getProductById(productId);
                model.addAttribute("pageTitle", "Cart ID |" + productId);
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
            } catch (CartLineItemException | UserException ex) {
                redirectAttributes.addFlashAttribute("message", ex.getMessage());
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
            } catch (UserException e) {
                throw new RuntimeException(e);
            }
        }

        @PostMapping("/cart/checkout")
        public String checkOutCart(@AuthenticationPrincipal ShopMeUserDetail customer,
                                   @RequestParam("productIds") List<String> productIds,
                                   @RequestParam("quantities") List<String> quantities,
                                   RedirectAttributes redirectAttributes) throws ProductException {
            try {
                User user = this.cartService.findUserById(customer.getUserId());
                this.cartService.checkOutCart(user, productIds, quantities);
                redirectAttributes.addFlashAttribute("Message", "Congratulation! You're Buy Successfully");
                return "redirect:/main-page";
            } catch (ProductException | UserException ex) {
                return "redirect:/main-page";
            } catch (CartLineItemException e) {
                throw new RuntimeException(e);
            }
        }

        @PostMapping("/cart/purchase")
        public String purchaseProducts(RedirectAttributes redirectAttributes,
                                       @AuthenticationPrincipal ShopMeUserDetail customer,
                                       @RequestParam(value = "productId", required = false) Integer productId,
                                       @RequestParam(value = "quantity", required = false) Integer quantity) {
            try {
                User customerUser = cartService.findUserById(customer.getUserId());
                orderService.purchaseProductDirect(productId, quantity, customerUser);
                redirectAttributes.addFlashAttribute("Message", "Congratulations on your successful purchase!");
            } catch (CartLineItemException | UserException | ProductException e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            }
            return "redirect:/main-page";
        }
        @PostMapping("/cart/purchase-in-cart")
        public String purchaseProductFromCart(
                Model model,
                @AuthenticationPrincipal ShopMeUserDetail customer,
                @RequestParam(value = "cartLineItemId",required = false) Integer cartLineItemId,
                @RequestParam(value = "quantity", required = false) Integer quantity,
                RedirectAttributes redirectAttributes) {
            try {
                User customerUser = this.cartService.findUserById(customer.getUserId());
                this.orderService.purchaseFromCart(cartLineItemId, quantity, customerUser);

            } catch (CartLineItemException |UserException e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                System.out.println("hia" + e.getMessage());
                return "redirect:/cart/shopping-cart";
            }
            System.out.println("h12ia");

            redirectAttributes.addFlashAttribute("Message", "Congratulation! You're Buy Successfully");
            return "redirect:/main-page";
        }

    }
