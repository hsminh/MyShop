package com.example.myshopdaknong.services;

import com.example.myshopdaknong.entity.*;
import com.example.myshopdaknong.exception.CardLineItemException;
import com.example.myshopdaknong.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private CartLineItemRepositoty cartLineItemRepositoty;

    @Autowired
    private CartReposttory cartReposttory;

    @Autowired
    private ProductsRepository productsRepository;

    public Order getOrderById(Users users)
    {
        return this.orderRepository.findByUsersId(users);
    }
    public String saveOrder(Integer id, Integer quantity, Users customer) throws CardLineItemException {
        Optional<CartLineItems> cartOptional = this.cartLineItemRepositoty.findById(id);
        if (cartOptional.isPresent()) {
            CartLineItems cartLineItemPayment = cartOptional.get();
            Float taxPerProduct = cartLineItemPayment.getTaxTotalAmount() / cartLineItemPayment.getQuantity();

            // Lấy Order
            Order order = this.orderRepository.findByUsersId(customer);
            if (order == null) {
                order = new Order();
                order.setUsersId(customer);
                order.setCountItems(0); // Đặt số lượng item ban đầu là 0
            }
            order.setCountItems(order.getCountItems() + quantity);

            // Lấy Product từ cơ sở dữ liệu để đảm bảo đối tượng được quản lý
            Product product = cartLineItemPayment.getProductId();
            product = this.productsRepository.findById(product.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Tính TotalAmount cho OrderLineItem
            Float totalAmount = (product.getDiscount_price() * quantity) + (taxPerProduct * quantity);

            // Tính TaxAmount cho OrderLineItem (đã tính taxPerProduct cho mỗi sản phẩm)
            Float taxAmount = taxPerProduct * quantity;

            // Tạo OrderLineItem
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setOrderId(order);
            orderLineItem.setProductId(product); // Gán Product đã lấy từ cơ sở dữ liệu
            orderLineItem.setQuantity(quantity);
            orderLineItem.setTaxTotalAmount(taxAmount);
            orderLineItem.setTotalAmount(totalAmount);
            orderLineItem.setSubTotalAmount(product.getDiscount_price() * quantity);

            // Cập nhật Order
            order.setTotalAmount(order.getTotalAmount() + totalAmount);
            order.setTaxAmount(order.getTaxAmount() + taxAmount);
            order.setUpdatedAt(new Date());
            order.setStatus(true);

            // Lấy Cart để cập nhật thông tin
            Cart updateCart = this.cartReposttory.findByUsersId(customer);
            updateCart.setTotal_amount(updateCart.getTotal_amount() - cartLineItemPayment.getTotalAmount());
            updateCart.setTax_amount(updateCart.getTax_amount() - cartLineItemPayment.getTaxTotalAmount());
            updateCart.setCount_items(updateCart.getCount_items() - cartLineItemPayment.getQuantity());
            if (updateCart.getCount_items() == 0) {
                updateCart.setDeletedAt(new Date());
            }

            cartLineItemPayment.setCartId(null);
            this.cartReposttory.save(updateCart);
            this.cartLineItemRepositoty.delete(cartLineItemPayment);
            this.orderRepository.save(order);
            this.orderLineItemRepository.save(orderLineItem);

            return "Buy Successfully";
        } else {
            throw new CardLineItemException("Cannot Found Cart With Id " + id);
        }
    }

}
