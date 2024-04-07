package com.example.sm.minh.eshop.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity(name = "orders")
@Getter
@Setter
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private float taxAmount;

    private Float totalAmount;

    private float countItem;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "status")
    @Temporal(TemporalType.TIMESTAMP)
    private Boolean status;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id")
    private User userId;

    public Order() {
        this.taxAmount=0;
        this.totalAmount=0f;
        this.countItem =0;
        this.createdAt=new Date();
        this.status=false;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", taxAmount=" + taxAmount +
                ", totalAmount=" + totalAmount +
                ", countItems=" + countItem +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", status=" + status +
                '}';
    }
}
