package com.sportstrust.hub.service;

import com.sportstrust.hub.dto.IDSubmitRequest;
import com.sportstrust.hub.dto.ProfileView;
import com.sportstrust.hub.entity.AthleteProfile;
import com.sportstrust.hub.entity.ModerationLog;
import com.sportstrust.hub.entity.User;
import com.sportstrust.hub.entity.enums.ActionType;
import com.sportstrust.hub.entity.enums.VerificationStatus;
import com.sportstrust.hub.exception.CustomException;
import com.sportstrust.hub.mapper.EntityMapper;
import com.sportstrust.hub.repository.AthleteProfileRepository;
import com.sportstrust.hub.repository.ModerationLogRepository;
import com.sportstrust.hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final AthleteProfileRepository athleteProfileRepository;
    private final UserRepository userRepository;
    private final ModerationLogRepository moderationLogRepository;
    private final EntityMapper entityMapper;

    public ProfileView getProfileByUserId(Long userId) {
        AthleteProfile profile = athleteProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException("Athlete profile not found", HttpStatus.NOT_FOUND));
        return entityMapper.toProfileView(profile);
    }

    @Transactional
    public void updateProfile(Long userId, com.sportstrust.hub.dto.ProfileUpdateRequest request) {
        AthleteProfile profile = athleteProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException("Athlete profile not found", HttpStatus.NOT_FOUND));
        
        profile.setSport(request.getSport());
        profile.setPosition(request.getPosition());
        if (request.getExperienceYears() != null) {
            profile.setExperienceYears(request.getExperienceYears());
        }
        athleteProfileRepository.save(profile);
    }

    @Transactional
    public void submitIdProof(Long userId, IDSubmitRequest request) {
        AthleteProfile profile = athleteProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException("Athlete profile not found", HttpStatus.NOT_FOUND));
        
        if (profile.getVerificationStatus() == VerificationStatus.VERIFIED) {
            throw new CustomException("Profile is already verified", HttpStatus.BAD_REQUEST);
        }

        profile.setIdProofUrl(request.getIdProofUrl());
        profile.setVerificationStatus(VerificationStatus.PENDING_VERIFICATION);
        athleteProfileRepository.save(profile);
    }

    public List<ProfileView> getPendingVerifications() {
        return athleteProfileRepository.findByVerificationStatus(VerificationStatus.PENDING_VERIFICATION)
                .stream()
                .map(entityMapper::toProfileView)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveVerification(Long adminId, Long profileId) {
        processVerification(adminId, profileId, VerificationStatus.VERIFIED, ActionType.APPROVE_ID, null);
    }

    @Transactional
    public void rejectVerification(Long adminId, Long profileId, String reason) {
        // reason is conceptually not logged cleanly since processVerification accepts ActionType but not reason
        // However, we can intercept or just rely on processVerification
        processVerification(adminId, profileId, VerificationStatus.REJECTED, ActionType.REJECT_ID, reason);
    }

    private void processVerification(Long adminId, Long profileId, VerificationStatus newStatus, ActionType actionType, String reason) {
        AthleteProfile profile = athleteProfileRepository.findById(profileId)
                .orElseThrow(() -> new CustomException("Athlete profile not found", HttpStatus.NOT_FOUND));
                
        profile.setVerificationStatus(newStatus);
        athleteProfileRepository.save(profile);

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new CustomException("Admin not found", HttpStatus.NOT_FOUND));

        String logDescription = "Changed verification status to " + newStatus.name();
        if (reason != null && !reason.trim().isEmpty()) {
            logDescription += ". Reason: " + reason;
        }

        ModerationLog log = ModerationLog.builder()
                .admin(admin)
                .actionType(actionType)
                .targetId(profileId)
                .description(logDescription)
                .build();
        moderationLogRepository.save(log);
    }
}
