package com.example.myshopdaknong.controller.Orders;

import com.example.myshopdaknong.entity.Order;
import com.example.myshopdaknong.entity.Users;
import com.example.myshopdaknong.repository.OrderLineItemRepository;
import com.example.myshopdaknong.repository.UserRepository;
import com.example.myshopdaknong.sercurity.ShopMeUserDetail;
import com.example.myshopdaknong.services.CartService;
import com.example.myshopdaknong.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @GetMapping("/order/success")
    public String orderSuccess(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("messageSuccessfull", "Congratulations on your successful purchase");
        return "redirect:/main-page"; // Redirect to main page after successful purchase
    }

        @GetMapping("/order/history")
        public String transactionHistory(Model model, @AuthenticationPrincipal ShopMeUserDetail user) {
            Optional<Users> optionalUser = this.userRepository.findById(user.getUserId());
            if (optionalUser.isPresent()) {
                Users customerLogin = optionalUser.get();
                Order order = this.orderService.getOrderById(customerLogin);
                model.addAttribute("order", order);
                model.addAttribute("listOrderLineItem", this.orderLineItemRepository.findByOrderId(order));
                return "order/transaction"; // Show transaction history
            }
            return "redirect:/login-form";
        }

    }
