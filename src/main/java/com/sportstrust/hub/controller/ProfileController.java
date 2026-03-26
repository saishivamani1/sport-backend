package com.sportstrust.hub.controller;

import com.sportstrust.hub.dto.ApiResponse;
import com.sportstrust.hub.dto.IDSubmitRequest;
import com.sportstrust.hub.dto.ProfileView;
import com.sportstrust.hub.dto.ProfileUpdateRequest;
import com.sportstrust.hub.entity.User;
import com.sportstrust.hub.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Profile & Verification", description = "Endpoints for athlete profiles and admin verification")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/verification/submit-id")
    @PreAuthorize("hasRole('ATHLETE')")
    @Operation(summary = "Submit ID proof for verification (Athlete Only)")
    public ResponseEntity<ApiResponse<Void>> submitIdProof(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody IDSubmitRequest request) {
        profileService.submitIdProof(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(null, "ID proof submitted successfully. Waiting for admin approval."));
    }

    @GetMapping("/verification/status")
    @PreAuthorize("hasRole('ATHLETE')")
    @Operation(summary = "Get current profile and verification status")
    public ResponseEntity<ApiResponse<ProfileView>> getProfileStatus(@AuthenticationPrincipal User user) {
        ProfileView profile = profileService.getProfileByUserId(user.getId());
        return ResponseEntity.ok(ApiResponse.success(profile, "Profile retrieved successfully"));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('ATHLETE')")
    @Operation(summary = "Update athlete profile (Athlete Only)")
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ProfileUpdateRequest request) {
        profileService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(null, "Profile updated successfully"));
    }

    // --- ADMIN ENDPOINTS ---

    @GetMapping("/admin/verifications/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all pending ID verifications (Admin Only)")
    public ResponseEntity<ApiResponse<List<ProfileView>>> getPendingVerifications() {
        List<ProfileView> pending = profileService.getPendingVerifications();
        return ResponseEntity.ok(ApiResponse.success(pending, "Pending verifications retrieved successfully"));
    }

    @PutMapping("/admin/verifications/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Approve an athlete's verification (Admin Only)")
    public ResponseEntity<ApiResponse<Void>> approveVerification(
            @AuthenticationPrincipal User admin,
            @PathVariable Long id) {
        profileService.approveVerification(admin.getId(), id);
        return ResponseEntity.ok(ApiResponse.success(null, "Verification approved successfully"));
    }

    @PutMapping("/admin/verifications/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reject an athlete's verification (Admin Only)")
    public ResponseEntity<ApiResponse<Void>> rejectVerification(
            @AuthenticationPrincipal User admin,
            @PathVariable Long id,
            @RequestParam String reason) {
        profileService.rejectVerification(admin.getId(), id, reason);
        return ResponseEntity.ok(ApiResponse.success(null, "Verification rejected successfully"));
    }
}
