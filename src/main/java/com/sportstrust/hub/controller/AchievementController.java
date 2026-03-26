package com.sportstrust.hub.controller;

import com.sportstrust.hub.dto.AchievementResponse;
import com.sportstrust.hub.dto.AchievementSubmitRequest;
import com.sportstrust.hub.dto.ApiResponse;
import com.sportstrust.hub.entity.User;
import com.sportstrust.hub.service.AchievementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Achievements & Posts", description = "Endpoints for athlete achievement posts")
public class AchievementController {

    private final AchievementService achievementService;

    @PostMapping("/posts/submit")
    @PreAuthorize("hasRole('ATHLETE')")
    @Operation(summary = "Submit a new achievement post (Verified Athlete Only)")
    public ResponseEntity<ApiResponse<AchievementResponse>> submitPost(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AchievementSubmitRequest request) {
        AchievementResponse response = achievementService.submitPost(user.getId(), request);
        return new ResponseEntity<>(ApiResponse.success(response, "Post submitted successfully. Waiting for review."), HttpStatus.CREATED);
    }

    @GetMapping("/posts/feed")
    @Operation(summary = "View approved achievements feed")
    public ResponseEntity<ApiResponse<List<AchievementResponse>>> getFeed() {
        List<AchievementResponse> feed = achievementService.getFeed();
        return ResponseEntity.ok(ApiResponse.success(feed, "Feed retrieved successfully"));
    }

    @GetMapping("/posts/my-posts")
    @PreAuthorize("hasRole('ATHLETE')")
    @Operation(summary = "View my achievement posts (Athlete Only)")
    public ResponseEntity<ApiResponse<List<AchievementResponse>>> getMyPosts(@AuthenticationPrincipal User user) {
        List<AchievementResponse> myPosts = achievementService.getMyPosts(user.getId());
        return ResponseEntity.ok(ApiResponse.success(myPosts, "My posts retrieved successfully"));
    }

    // --- ADMIN ENDPOINTS ---

    @GetMapping("/admin/posts/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all pending achievement posts (Admin Only)")
    public ResponseEntity<ApiResponse<List<AchievementResponse>>> getPendingPosts() {
        List<AchievementResponse> pending = achievementService.getPendingPosts();
        return ResponseEntity.ok(ApiResponse.success(pending, "Pending posts retrieved successfully"));
    }

    @PutMapping("/admin/posts/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Approve a pending achievement post (Admin Only)")
    public ResponseEntity<ApiResponse<Void>> approvePost(
            @AuthenticationPrincipal User admin,
            @PathVariable Long id) {
        achievementService.reviewPost(admin.getId(), id, true, "Approved");
        return ResponseEntity.ok(ApiResponse.success(null, "Post approved successfully"));
    }

    @PutMapping("/admin/posts/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reject a pending achievement post (Admin Only)")
    public ResponseEntity<ApiResponse<Void>> rejectPost(
            @AuthenticationPrincipal User admin,
            @PathVariable Long id,
            @RequestParam String reason) {
        achievementService.reviewPost(admin.getId(), id, false, reason);
        return ResponseEntity.ok(ApiResponse.success(null, "Post rejected successfully"));
    }
}
