package com.sportstrust.hub.repository;

import com.sportstrust.hub.entity.AthleteProfile;
import com.sportstrust.hub.entity.User;
import com.sportstrust.hub.entity.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AthleteProfileRepository extends JpaRepository<AthleteProfile, Long> {
    Optional<AthleteProfile> findByUser(User user);
    Optional<AthleteProfile> findByUserId(Long userId);
    List<AthleteProfile> findByVerificationStatus(VerificationStatus status);
}
