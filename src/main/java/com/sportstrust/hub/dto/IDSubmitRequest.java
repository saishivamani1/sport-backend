package com.sportstrust.hub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IDSubmitRequest {

    @NotBlank(message = "ID Proof URL is required")
    private String idProofUrl;
}
