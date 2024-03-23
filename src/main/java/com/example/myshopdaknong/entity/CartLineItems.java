package com.example.myshopdaknong.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class CartLineItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private float quantity;

    @Column(name = "total_amount", nullable = false)
    private float totalAmount;

    @Column(name = "sub_total_amount", nullable = false)
    private float subTotalAmount;

    @Column(name = "tax_total_amount", nullable = false)
    private float taxTotalAmount;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cartId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product productId;

    public CartLineItems() {

        this.quantity=0;
        this.totalAmount=0;
        this.subTotalAmount=0;
        this.taxTotalAmount=0;
        this.createdAt=new Date();
    }

}
