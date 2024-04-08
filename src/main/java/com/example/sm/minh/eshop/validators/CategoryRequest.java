package com.example.sm.minh.eshop.validators;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
@Getter
@Setter
public class CategoryRequest {
    private Integer id;

    @NotNull
    @NotBlank(message = "Name Is Required")
    @Length(min = 2,max = 100,message = "Name must be at least 2 characters")
    private String name ;

    @NotNull
    @NotBlank(message = "Slug Is Required")
    @Length(min = 2, max = 100, message = "Slug must be between 2 and 100 characters")
    private String slug ;

    @NotNull
    @NotBlank(message = "Description Is Required")
    @Length(min = 6,message = "description must be at least 6 characters")
    private String description;
    private Boolean isActive;

    public CategoryRequest() {
    }
}
