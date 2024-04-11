package com.example.sm.minh.eshop.controllers.Order;

import com.example.sm.minh.eshop.exceptions.UserException;
import com.example.sm.minh.eshop.models.User;
import com.example.sm.minh.eshop.exceptions.CartLineItemException;
import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.securities.ShopMeUserDetail;
import com.example.sm.minh.eshop.services.CartService;
import com.example.sm.minh.eshop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;




}
