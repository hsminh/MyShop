package com.example.sm.minh.eshop.validators.annotations;

import com.example.sm.minh.eshop.validators.constraints.UniqueSlugAndName;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueSlugAndName.class)
@Documented
public @interface ValidateCategorySlugAndName {
    String message() default "Email address must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String nameField()default "";
    String idField()default "";
    String SlugField()default "";
}
