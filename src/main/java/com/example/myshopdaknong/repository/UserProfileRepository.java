package com.example.myshopdaknong.repository;

import com.example.myshopdaknong.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {
}
