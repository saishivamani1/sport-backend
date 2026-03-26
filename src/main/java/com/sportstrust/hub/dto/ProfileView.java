package com.sportstrust.hub.dto;

import com.sportstrust.hub.entity.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileView {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String sport;
    private String position;
    private Integer experienceYears;
    private VerificationStatus verificationStatus;
    private Double credibilityScore;
    private Integer totalTournaments;
    private Integer totalWins;
}
