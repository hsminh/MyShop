package com.example.sm.minh.eshop.validators;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
public class UserRequest {
    private Integer id;
    @NotNull
    @NotBlank(message = "User Name Is Required")
    @Length(min = 6,message = "User Name must be at least 6 characters")
    @Email(message = "Invalid email address")
    private String userName;

    @NotBlank(message = "Password Is Required")
    @Length(min = 6,max = 128,message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "First Name Is Required")
    private String firstName;

    @NotBlank(message = "Last Name Is Required")
    private String lastName;

    private Boolean isActive;

    public UserRequest() {
    }
}
