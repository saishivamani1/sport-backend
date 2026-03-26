package com.sportstrust.hub.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileUpdateRequest {
    @NotBlank(message = "Sport name is required")
    private String sport;

    private String position;

    @Min(value = 0, message = "Experience years cannot be negative")
    private Integer experienceYears;
}
