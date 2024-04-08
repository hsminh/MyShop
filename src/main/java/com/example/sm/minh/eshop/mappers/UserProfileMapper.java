package com.example.sm.minh.eshop.mappers;

import com.example.sm.minh.eshop.models.UserProfile;
import com.example.sm.minh.eshop.validators.UserProfileRequest;

public class UserProfileMapper {
    public static UserProfile toUserProfile(UserProfileRequest userProfileRequest) {
        UserProfile userProfile = new UserProfile();
        userProfile.setAddress(userProfileRequest.getAddress());
        userProfile.setBio(userProfileRequest.getBio());
        userProfile.setPhoneNumber(userProfileRequest.getPhoneNumber());
        userProfile.setGender(userProfileRequest.isGender());
        userProfile.setId(userProfileRequest.getId());
        return userProfile;
    }

    public static UserProfileRequest toUserProfileRequest(UserProfile userProfile) {
        UserProfileRequest userProfileRequest = new UserProfileRequest();
        userProfileRequest.setAddress(userProfile.getAddress());
        userProfileRequest.setBio(userProfile.getBio());
        userProfileRequest.setPhoneNumber(userProfile.getPhoneNumber());
        userProfileRequest.setGender(userProfile.isGender());
        userProfileRequest.setId(userProfile.getId());
        userProfileRequest.setId(userProfile.getId());
        return userProfileRequest;
    }
}
