package com.example.myshopdaknong.Repository;

import com.example.myshopdaknong.Entity.user_profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface user_ProfileRepository extends JpaRepository<user_profile,Integer> {
}
