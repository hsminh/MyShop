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
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private float tax_amount;

    private Float total_amount;

    private float count_items;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;
    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;
    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id")
    private Users usersId;

    public Cart() {
        this.count_items=0;
        this.createdAt=new Date();
        this.tax_amount=0;
        this.total_amount=0f;


    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", tax_amount=" + tax_amount +
                ", total_amount=" + total_amount +
                ", count_items=" + count_items +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", createdAt=" + createdAt +
                ", isActive=" + isActive +
                ", usersId=" + usersId +
                '}';
    }
}
