    package com.example.myshopdaknong.controller.Orders;

    import com.example.myshopdaknong.entity.Order;
    import com.example.myshopdaknong.entity.OrderLineItem;
    import com.example.myshopdaknong.entity.User;
    import com.example.myshopdaknong.exception.UserNotFoundException;
    import com.example.myshopdaknong.repository.OrderLineItemRepository;
    import com.example.myshopdaknong.repository.UserRepository;
    import com.example.myshopdaknong.sercurity.ShopMeUserDetail;
    import com.example.myshopdaknong.services.CartService;
    import com.example.myshopdaknong.services.OrderService;
    import com.example.myshopdaknong.services.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    import java.util.List;
    import java.util.Optional;

    @Controller
    public class OrderController {
        @Autowired
        private OrderService orderService;

        @Autowired
        private UserService userService;


        @GetMapping("/order/success")
        public String orderSuccess(RedirectAttributes redirectAttributes) {
            redirectAttributes.addFlashAttribute("messageSuccessfully", "Congratulations on your successful purchase");
            return "redirect:/main-page";
        }

        @GetMapping("/order/history/{pageNum}")
        public String viewPurchaseHistory(@PathVariable("pageNum") Integer pageNum, Model model, @AuthenticationPrincipal ShopMeUserDetail user) throws UserNotFoundException {
            User customerLogin = this.userService.findUserById(user.getUserId());
            Order order = this.orderService.getOrderById(customerLogin);
            Page<OrderLineItem> listOrderLineItemPage = this.orderService.findByOrderId(order, 0);
            int totalPage = listOrderLineItemPage.getTotalPages();

            if (pageNum > totalPage) {
                pageNum = totalPage;
            }
            if (pageNum < 1) {
                pageNum = 1;
            }
            Page<OrderLineItem> listOrderLineItem = this.orderService.findByOrderId(order, pageNum - 1);
            model.addAttribute("pageNum", pageNum);
            model.addAttribute("order", order);
            model.addAttribute("totalPage", totalPage);
            model.addAttribute("listOrderLineItem", listOrderLineItem.getContent());

            return "order/transaction";
        }



    }
