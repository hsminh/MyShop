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
import java.util.HashSet;
import java.util.Set;

@Entity(name = "product_categories")
@Getter
@Setter
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotBlank(message = "Name Is Required")
    @Length(min = 2,max = 100,message = "Name must be at least 2 characters")
    @Column(name = "name",length = 255,nullable = false,unique = true)
    private String name ;

    @NotNull
    @NotBlank(message = "Slug Is Required")
    @Length(min = 2, max = 100, message = "Slug must be between 2 and 100 characters")
    @Column(name = "slug",length = 100,nullable = false,unique = true)
    private String slug ;

    @NotNull
    @NotBlank(message = "Description Is Required")
    @Length(min = 6,message = "description must be at least 6 characters")
    @Column(name = "description",length = 255,nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany(mappedBy = "ListProductCategories" ,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Product> LiProducts = new HashSet<>();

    public ProductCategory(String name, String slug, String description) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.createdAt=new Date();
    }

    public ProductCategory() {
    }
    public String getLog() {
        return this.id +" " +this.name +" "+this.slug ;
    }
    @Override
    public String toString() {
        return this.name;
    }
}
