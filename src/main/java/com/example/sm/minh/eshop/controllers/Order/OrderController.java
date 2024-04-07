    package com.example.sm.minh.eshop.controllers.Order;

    import com.example.sm.minh.eshop.entities.Order;
    import com.example.sm.minh.eshop.entities.OrderLineItem;
    import com.example.sm.minh.eshop.entities.User;
    import com.example.sm.minh.eshop.exceptions.OrderLineItemException;
    import com.example.sm.minh.eshop.exceptions.UserNotFoundException;
    import com.example.sm.minh.eshop.securities.ShopMeUserDetail;
    import com.example.sm.minh.eshop.services.OrderService;
    import com.example.sm.minh.eshop.services.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    @Controller
    public class OrderController {
        @Autowired
        private OrderService orderService;

        @Autowired
        private UserService userService;


        @GetMapping("/order/success")
        public String orderSuccess(RedirectAttributes redirectAttributes) {
            redirectAttributes.addFlashAttribute("Message", "Congratulations on your successful purchase");
            return "redirect:/main-page";
        }
        @GetMapping("/order/detail")
        public String viewOrderDetail(@RequestParam("oder-line-item-id")Integer oderLineItemId,Model model, RedirectAttributes redirectAttributes) throws OrderLineItemException {
            try
            {
                model.addAttribute("OderLineItem",this.orderService.findOrderLineItemById(oderLineItemId));
                return "order/order-detail";
            }catch (OrderLineItemException ex)
            {
                redirectAttributes.addFlashAttribute("errorMessage","Cannot Find Order With Id "+oderLineItemId);
                return "redirect:/order/history/1";
            }
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
