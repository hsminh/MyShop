package com.example.sm.minh.eshop.services;

import com.example.sm.minh.eshop.entities.*;
import com.example.sm.minh.eshop.exceptions.CardLineItemException;
import com.example.sm.minh.eshop.exceptions.OrderLineItemException;
import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.repositories.CartReposttory;
import com.example.sm.minh.eshop.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private ProductRepository productsRepository;

    private static Integer PAGE_SIZE=5;

    public Order getOrderById(User users)
    {
        return this.orderRepository.findByUserId(users);
    }
    public String saveOrder(Integer id, Integer quantity, User customer) throws CardLineItemException {
        Optional<CartLineItem> cartOptional = this.cartLineItemRepositoty.findById(id);
        if (cartOptional.isPresent()) {
            CartLineItem cartLineItemPayment = cartOptional.get();
            Float taxPerProduct = cartLineItemPayment.getTaxTotalAmount() / cartLineItemPayment.getQuantity();

            Order order = this.orderRepository.findByUserId(customer);
            if (order == null) {
                order = new Order();
                order.setUserId(customer);
                order.setCountItem(0);
            }
            order.setCountItem(order.getCountItem() + quantity);

            Product product = cartLineItemPayment.getProductId();
            product = this.productsRepository.findById(product.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Float totalAmount = (product.getDiscountPrice() * quantity) + (taxPerProduct * quantity);

            Float taxAmount = taxPerProduct * quantity;

            OrderLineItem orderLineItem=this.setOrderLineItem(order,product,quantity,taxAmount,totalAmount);

            order = updateOrder(order, totalAmount, taxAmount);

            Cart updateCart = this.cartReposttory.findByUserId(customer);
            updateCart.setTotalAmount(updateCart.getTotalAmount() - cartLineItemPayment.getTotalAmount());
            updateCart.setTaxAmount(updateCart.getTaxAmount() - cartLineItemPayment.getTaxTotalAmount());
            updateCart.setCountItem(updateCart.getCountItem() - cartLineItemPayment.getQuantity());
            if (updateCart.getCountItem() == 0) {
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
            orderLineItem.setSubTotalAmount(product.getDiscountPrice() * quantity);
            return orderLineItem;
        }

    public String saveOrderDirect(Integer productId, Integer quantity, User customer) throws CardLineItemException, ProductException {
                Optional<Product> selectProduct=this.productsRepository.findById(productId);
                if(selectProduct.isPresent())
                {
                    Product productSelect=selectProduct.get();
                    Order order = this.orderRepository.findByUserId(customer);
                    if (order == null) {
                        order = new Order();
                        order.setUserId(customer);
                        order.setCountItem(0);
                    }
                    order.setCountItem(order.getCountItem() + quantity);
                    Float taxPerProduct=(productSelect.getDiscountPrice()/100)*productSelect.getTax();
                    Float totalAmount = (productSelect.getDiscountPrice() * quantity) + (taxPerProduct * quantity);
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

    public OrderLineItem findOrderLineItemById(Integer oderLineItemId) throws OrderLineItemException {
        Optional<OrderLineItem>OderLineItem=this.orderLineItemRepository.findById(oderLineItemId);
        if(OderLineItem.isPresent())
        {
            return OderLineItem.get();
        }else
        {
            throw new OrderLineItemException("Cannot Found Oder Line Item With Id "+oderLineItemId);
        }
    }
}