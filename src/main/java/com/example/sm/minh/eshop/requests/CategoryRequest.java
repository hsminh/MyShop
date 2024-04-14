package com.example.sm.minh.eshop.requests;

import com.example.sm.minh.eshop.validators.annotations.SlugAndNameUnique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@SlugAndNameUnique(idField = "id",SlugField ="slug", nameField = "name")
public class CategoryRequest  {
    private Integer id;

    @NotNull
    @NotBlank(message = "Name Is Required")
    @Length(min = 2, message = "name must be at least 2 characters")
    @Length(max =100, message = "name must not exceed 100 characters")
    private String name ;

    @NotNull
    @NotBlank(message = "Slug Is Required")
    @Length(min = 2, message = "slug must be at least 2 characters")
    @Length(max = 30, message = "slug must not exceed 30 characters")
    private String slug ;

    @NotNull
    @NotBlank(message = "Description Is Required")
    @Length(min = 6, message = "description must be at least 6 characters")
    @Length(max = 255, message = "description must not exceed 255 characters")
    private String description;
    private Boolean isActive;

    public CategoryRequest() {
    }


}
