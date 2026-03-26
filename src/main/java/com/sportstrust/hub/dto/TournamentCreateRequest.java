package com.sportstrust.hub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentCreateRequest {

    @NotBlank(message = "Tournament name is required")
    private String name;

    @NotBlank(message = "Sport category is required")
    private String sport;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Start date is required")
    @jakarta.validation.constraints.FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @jakarta.validation.constraints.FutureOrPresent(message = "End date must be today or in the future")
    private LocalDate endDate;

    @NotNull(message = "Registration deadline is required")
    @jakarta.validation.constraints.FutureOrPresent(message = "Registration deadline must be today or in the future")
    private LocalDate registrationDeadline;

    private String description;
    private Integer maxParticipants;
    private String prizePool;
}
