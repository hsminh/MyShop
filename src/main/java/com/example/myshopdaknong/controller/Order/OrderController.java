package com.example.myshopdaknong.controller.Order;

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
        redirectAttributes.addFlashAttribute("messageSuccessfull","Congratulations on your successful purchase");
        return "redirect:/main-page"; // Trả về trang order_success.html sau khi mua hàng thành công
    }
    @GetMapping("/order/history")
    public String transactionHistory(Model model,@AuthenticationPrincipal ShopMeUserDetail user) {
        Optional<Users> Customer=this.userRepository.findById(user.getUserId());
        if(Customer.isPresent())
        {
            Users customerLogin=Customer.get();
            Order order=this.orderService.getOrderById(customerLogin);
            model.addAttribute("order",order) ;
            System.out.println("here");

            model.addAttribute("listOderLineItem",this.orderLineItemRepository.findByOrderId(order));
            return "order/transaction"; // Trả về trang order_success.html sau khi mua hàng thành công
        }
        return "cc";
    }

}
