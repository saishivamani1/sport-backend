package com.sportstrust.hub.service;

import com.sportstrust.hub.dto.ProfileView;
import com.sportstrust.hub.entity.AthleteProfile;
import com.sportstrust.hub.entity.enums.VerificationStatus;
import com.sportstrust.hub.mapper.EntityMapper;
import com.sportstrust.hub.repository.AthleteProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscoveryService {

    private final AthleteProfileRepository athleteProfileRepository;
    private final EntityMapper entityMapper;

    // A simple discovery logic based on requirements
    public List<ProfileView> searchAthletes(String sport, Double minCredibility) {
        List<AthleteProfile> profiles = athleteProfileRepository.findAll();

        return profiles.stream()
                .filter(p -> p.getVerificationStatus() == VerificationStatus.VERIFIED)
                .filter(p -> sport == null || sport.equalsIgnoreCase(p.getSport()))
                .filter(p -> minCredibility == null || (p.getCredibilityScore() != null && p.getCredibilityScore() >= minCredibility))
                .map(entityMapper::toProfileView)
                .collect(Collectors.toList());
    }

    public ProfileView getAthleteDetails(Long id) {
        AthleteProfile profile = athleteProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return entityMapper.toProfileView(profile);
    }
}
