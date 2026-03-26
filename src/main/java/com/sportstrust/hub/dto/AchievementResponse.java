package com.sportstrust.hub.dto;

import com.sportstrust.hub.entity.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementResponse {
    private Long id;
    private Long athleteId;
    private String athleteName;
    private String title;
    private String description;
    private String proofUrl;
    private PostStatus status;
    private LocalDateTime submittedAt;
}
