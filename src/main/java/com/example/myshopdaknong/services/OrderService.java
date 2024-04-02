package com.example.myshopdaknong.services;

import com.example.myshopdaknong.entity.*;
import com.example.myshopdaknong.exception.CardLineItemException;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
    private ProductRepository productsRepository;

    private static Integer PAGE_SIZE=3;

    public Order getOrderById(User users)
    {
        return this.orderRepository.findByUsersId(users);
    }
    public String saveOrder(Integer id, Integer quantity, User customer) throws CardLineItemException {
        Optional<CartLineItem> cartOptional = this.cartLineItemRepositoty.findById(id);
        if (cartOptional.isPresent()) {
            CartLineItem cartLineItemPayment = cartOptional.get();
            Float taxPerProduct = cartLineItemPayment.getTaxTotalAmount() / cartLineItemPayment.getQuantity();

            Order order = this.orderRepository.findByUsersId(customer);
            if (order == null) {
                order = new Order();
                order.setUsersId(customer);
                order.setCountItems(0);
            }
            order.setCountItems(order.getCountItems() + quantity);

            Product product = cartLineItemPayment.getProductId();
            product = this.productsRepository.findById(product.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Float totalAmount = (product.getDiscount_price() * quantity) + (taxPerProduct * quantity);

            Float taxAmount = taxPerProduct * quantity;

            OrderLineItem orderLineItem=this.setOrderLineItem(order,product,quantity,taxAmount,totalAmount);

            order = updateOrder(order, totalAmount, taxAmount);

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
            this.orderLineItemRepository.save(orderLineItem);

            return "Buy Successfully";
        } else {
            throw new CardLineItemException("Cannot Found Cart With Id " + id);
        }
    }
    public Order updateOrder(Order order, Float totalAmount, Float taxAmount) {
        // update infor order
        order.setTotalAmount(order.getTotalAmount() + totalAmount);
        order.setTaxAmount(order.getTaxAmount() + taxAmount);
        order.setUpdatedAt(new Date());
        order.setStatus(true);

        return this.orderRepository.save(order);
    }

    public OrderLineItem setOrderLineItem(Order order,Product product,int quantity,Float taxAmount,Float totalAmount)
        {
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setOrderId(order);
            orderLineItem.setProductId(product);
            orderLineItem.setQuantity(quantity);
            orderLineItem.setTaxTotalAmount(taxAmount);
            orderLineItem.setTotalAmount(totalAmount);
            orderLineItem.setSubTotalAmount(product.getDiscount_price() * quantity);
            return orderLineItem;
        }

    public String saveOrderDirect(Integer productId, Integer quantity, User customer) throws CardLineItemException, ProductException {
                Optional<Product> selectProduct=this.productsRepository.findById(productId);
                if(selectProduct.isPresent())
                {
                    Product productSelect=selectProduct.get();
                    // Lấy Order
                    Order order = this.orderRepository.findByUsersId(customer);
                    if (order == null) {
                        order = new Order();
                        order.setUsersId(customer);
                        order.setCountItems(0);
                    }
                    order.setCountItems(order.getCountItems() + quantity);

                    Float taxPerProduct=(productSelect.getDiscount_price()/100)*productSelect.getTax();
                    Float totalAmount = (productSelect.getDiscount_price() * quantity) + (taxPerProduct * quantity);

                    Float taxAmount = taxPerProduct * quantity;

                    OrderLineItem orderLineItem=this.setOrderLineItem(order,productSelect,quantity,taxAmount,totalAmount);

                    order = updateOrder(order, totalAmount, taxAmount);

                    this.orderLineItemRepository.save(orderLineItem);
                    return "Buy Successfully";
                }else
                {
                    throw new ProductException("Cannot Found Product With Id "+productId);

        }
    }


    public Page<OrderLineItem> findByOrderId(Order order, int pageNumber) {
        Sort sort=Sort.by("created_at");
        sort=sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        return this.orderLineItemRepository.findByOrderId(order,pageable);
    }
}