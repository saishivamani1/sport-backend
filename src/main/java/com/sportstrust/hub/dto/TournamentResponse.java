package com.sportstrust.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentResponse {
    private Long id;
    private String name;
    private String sport;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate registrationDeadline;
    private String description;
    private Integer maxParticipants;
    private String prizePool;
}
