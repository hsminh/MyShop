package com.example.myshopdaknong.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

public class ProductDTO {
    private Integer id;
    private Product product;
    private Long quantiryPurchase;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getQuantiryPurchase() {
        return quantiryPurchase;
    }

    public void setQuantiryPurchase(Long quantiryPurchase) {
        this.quantiryPurchase = quantiryPurchase;
    }

    public ProductDTO(Integer id,Product product, Long quantiryPurchase) {
        this.id=id;
        this.product = product;
        this.quantiryPurchase = quantiryPurchase;
    }
}
