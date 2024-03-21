package com.example.myshopdaknong.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Product_categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name",length = 255,nullable = false,unique = true)
    private String name ;

    @Column(name = "slug",length = 100,nullable = false,unique = true)
    private String slug ;

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

    public Product_categories(String name, String slug, String description) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.createdAt=new Date();
    }

    public Product_categories() {
    }

    @Override
    public String toString() {
        return "Product_categories{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", isActive=" + isActive +
                '}';
    }
}
