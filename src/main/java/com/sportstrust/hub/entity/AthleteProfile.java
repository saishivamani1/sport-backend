package com.sportstrust.hub.entity;

import com.sportstrust.hub.entity.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "athlete_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AthleteProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String sport;

    private String position;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    @Builder.Default
    private VerificationStatus verificationStatus = VerificationStatus.BASIC;

    @Column(name = "id_proof_url")
    private String idProofUrl;

    @Column(name = "credibility_score", nullable = false)
    @Builder.Default
    private Double credibilityScore = 0.0;

    @Column(name = "total_tournaments", nullable = false)
    @Builder.Default
    private Integer totalTournaments = 0;

    @Column(name = "total_wins", nullable = false)
    @Builder.Default
    private Integer totalWins = 0;
}
