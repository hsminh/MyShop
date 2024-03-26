package com.example.myshopdaknong.services;

import com.example.myshopdaknong.entity.*;
import com.example.myshopdaknong.exception.CardLineItemException;
import com.example.myshopdaknong.exception.ProductException;
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
            OrderLineItem orderLineItem=this.setOrderLineItem(order,product,quantity,taxAmount,totalAmount);

            // Cập nhật thông tin Order và lưu lại vào cơ sở dữ liệu
            order = updateOrder(order, totalAmount, taxAmount);

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
            this.orderLineItemRepository.save(orderLineItem);

            return "Buy Successfully";
        } else {
            throw new CardLineItemException("Cannot Found Cart With Id " + id);
        }
    }
    public Order updateOrder(Order order, Float totalAmount, Float taxAmount) {
        // Cập nhật thông tin Order
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
            orderLineItem.setProductId(product); // Gán Product đã lấy từ cơ sở dữ liệu
            orderLineItem.setQuantity(quantity);
            orderLineItem.setTaxTotalAmount(taxAmount);
            orderLineItem.setTotalAmount(totalAmount);
            orderLineItem.setSubTotalAmount(product.getDiscount_price() * quantity);
            return orderLineItem;
        }

    public String saveOrderDirect(Integer productId, Integer quantity, Users customer) throws CardLineItemException, ProductException {
                Optional<Product> selectProduct=this.productsRepository.findById(productId);
                if(selectProduct.isPresent())
                {
                    Product productSelect=selectProduct.get();
                    // Lấy Order
                    Order order = this.orderRepository.findByUsersId(customer);
                    if (order == null) {
                        order = new Order();
                        order.setUsersId(customer);
                        order.setCountItems(0); // Đặt số lượng item ban đầu là 0
                    }
                    order.setCountItems(order.getCountItems() + quantity);

                    // Lấy Product từ cơ sở dữ liệu để đảm bảo đối tượng được quản lý
                    Float taxPerProduct=(productSelect.getDiscount_price()/100)*productSelect.getTax();
                    // Tính TotalAmount cho OrderLineItem
                    Float totalAmount = (productSelect.getDiscount_price() * quantity) + (taxPerProduct * quantity);

                    // Tính TaxAmount cho OrderLineItem (đã tính taxPerProduct cho mỗi sản phẩm)
                    Float taxAmount = taxPerProduct * quantity;

                    // Tạo OrderLineItem
                    OrderLineItem orderLineItem=this.setOrderLineItem(order,productSelect,quantity,taxAmount,totalAmount);

                    // Cập nhật Order
                    // Cập nhật thông tin Order và lưu lại vào cơ sở dữ liệu
                    order = updateOrder(order, totalAmount, taxAmount);

                    this.orderLineItemRepository.save(orderLineItem);
                    return "Buy Successfully";
                }else
                {
                    throw new ProductException("Cannot Found Product With Id "+productId);

        }
    }
}