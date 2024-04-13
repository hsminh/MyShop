package com.example.sm.minh.eshop.validators.annotations;

import com.example.sm.minh.eshop.validators.constraints.UniqueEmailConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailConstraintValidator.class)
@Documented
public @interface ValidateIdAndUsername {
    String message() default "Email address must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String idField()default "";
    String emailField()default "";
}


