package com.example.sm.minh.eshop.validators;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileRequest {

    private Integer id;

    @NotNull
    @NotBlank(message = "Phone Number Is Required!")
    @Size(min = 10,max = 10,message = "Please enter a valid phone number!")
    private String phoneNumber;


    private String bio;

    @Override
    public String   toString() {
        return "UserProfileRequest{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", bio='" + bio + '\'' +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                '}';
    }

    @NotNull
    @NotBlank(message = "address Is Required!")
    @Size(min = 6, message = "Address must have at least 6 characters!")
    @Column(name = "addresss",nullable = false)
    private String address;

    private boolean gender;





}
