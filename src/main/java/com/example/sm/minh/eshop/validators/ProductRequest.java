package com.example.sm.minh.eshop.validators;

import com.example.sm.minh.eshop.models.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ProductRequest {
    private Integer id;

    @NotNull
    @NotBlank(message = "Name Is Required")
    @Length(min = 2,max = 100,message = "Name must be at least 2 characters")
    private String name ;

    @NotNull
    @NotBlank(message = "SKU Is Required")
    @Length(min = 2,max = 15,message = "SKU  must be between 2 and 15 characters")
    private String sku ;

    @NotNull
    @NotBlank(message = "Content Is Required")
    @Length(min = 10,message = "Content must be at least 10 characters")
    private String content ;

    private String image;

    private float price;

    private float discountPrice;

    private float tax;
    private Boolean isActive;
    private Set<ProductCategory> ListProductCategories = new HashSet<>();

    public ProductRequest() {
    }
    public String loadImages()
    {
        if(this.image==null||this.image.isEmpty())
        {
            return "/images/img.png";
        }else
        {
            return "/images/"+this.id+"/"+this.image;
        }
    }
}
