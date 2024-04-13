
package com.example.sm.minh.eshop.validators.annotations;

import com.example.sm.minh.eshop.validators.constraints.UniqueEmailAndSku;
import com.example.sm.minh.eshop.validators.constraints.UniqueEmailConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = UniqueEmailAndSku.class)
    @Documented
    public @interface ValidateSkuAndName {
        String message() default "";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};

        String nameField()default "";
        String skuField()default "";
        String idField()default "";

    }



