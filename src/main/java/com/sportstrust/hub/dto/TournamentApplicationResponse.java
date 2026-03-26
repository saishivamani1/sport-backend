package com.sportstrust.hub.dto;

import com.sportstrust.hub.entity.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentApplicationResponse {
    private Long id;
    private Long athleteId;
    private String athleteName;
    private Long tournamentId;
    private String tournamentName;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
}
