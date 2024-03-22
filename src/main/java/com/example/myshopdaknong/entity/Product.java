package com.example.myshopdaknong.entity;

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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name",length = 100,unique = true)
    private String name ;

    @Column(name = "sku",length = 15,unique = true)
    private String sku ;

    @Column(name = "content")
    @Lob
    private String content ;

    @Column(name = "image",length = 255)
    private String image;

    @Column(name = "price")
    private float price;

    @Column(name = "discount_price")
    private float discount_price;

    @Column(name = "tax")
    private float tax;

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

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "products_categories",
            joinColumns = { @JoinColumn(name = "product_categories_id") },
            inverseJoinColumns = { @JoinColumn(name = "product_id") }
    )
    private Set<ProductCategory> ListProductCategories = new HashSet<>();

    public String loadImages()
    {
        if(this.image==null||this.image.isEmpty())
        {
            return "images/img.png";
        }else
        {
            return "images/"+this.id+"/"+this.image;
        }
    }
//    public String loadImages() {
//        if (this.image == null || this.image.isEmpty()) {
//            return "images/img.png";
//        } else {
//            String imagePath = "images/";
//            if (this.id != null) {
//                imagePath += this.id + "/";
//            }
//            imagePath += this.image;
//            return imagePath;
//        }
//    }
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Sku='" + sku + '\'' +
                ", Content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", discount_price=" + discount_price +
                ", tax=" + tax +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", isActive=" + isActive +
                '}';
    }

    public Product(String name, String sku, String content, float price, float discount_price, float tax) {
        this.name = name;
        sku = sku;
        content = content;
        this.price = price;
        this.discount_price = discount_price;
        this.tax = tax;
        this.createdAt=new Date();
    }
    public void addProductCate(ProductCategory productCategories)
    {
        this.ListProductCategories.add(productCategories);
    }

    public Product() {

    }
}
