package com.sportstrust.hub.controller;

import com.sportstrust.hub.dto.ApiResponse;
import com.sportstrust.hub.dto.TournamentApplicationResponse;
import com.sportstrust.hub.dto.TournamentCreateRequest;
import com.sportstrust.hub.dto.TournamentResponse;
import com.sportstrust.hub.entity.User;
import com.sportstrust.hub.service.TournamentService;
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
@RequestMapping("/tournaments")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Tournaments", description = "Endpoints for managing and joining tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Or Organizer role
    @Operation(summary = "Create a new tournament (Admin Only)")
    public ResponseEntity<ApiResponse<TournamentResponse>> createTournament(@Valid @RequestBody TournamentCreateRequest request) {
        TournamentResponse response = tournamentService.createTournament(request);
        return new ResponseEntity<>(ApiResponse.success(response, "Tournament created successfully"), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all tournaments")
    public ResponseEntity<ApiResponse<List<TournamentResponse>>> getAllTournaments() {
        List<TournamentResponse> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(ApiResponse.success(tournaments, "Tournaments retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tournament details by ID")
    public ResponseEntity<ApiResponse<TournamentResponse>> getTournamentById(@PathVariable Long id) {
        TournamentResponse tournament = tournamentService.getTournamentById(id);
        return ResponseEntity.ok(ApiResponse.success(tournament, "Tournament retrieved successfully"));
    }

    @PostMapping("/{id}/apply")
    @PreAuthorize("hasRole('ATHLETE')")
    @Operation(summary = "Apply to participate in a tournament (Athlete Only)")
    public ResponseEntity<ApiResponse<TournamentApplicationResponse>> applyForTournament(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        TournamentApplicationResponse response = tournamentService.applyForTournament(user.getId(), id);
        return ResponseEntity.ok(ApiResponse.success(response, "Applied for tournament successfully"));
    }

    // --- ADMIN ENDPOINTS ---

    @GetMapping("/admin/applications")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all tournament applications (Admin Only)")
    public ResponseEntity<ApiResponse<List<com.sportstrust.hub.dto.TournamentApplicationResponse>>> getAllApplications() {
        List<com.sportstrust.hub.dto.TournamentApplicationResponse> responses = tournamentService.getAllApplications();
        return ResponseEntity.ok(ApiResponse.success(responses, "Applications retrieved successfully"));
    }

    @GetMapping("/admin/tournaments/{tournamentId}/applications")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get applications for a specific tournament (Admin Only)")
    public ResponseEntity<ApiResponse<List<com.sportstrust.hub.dto.TournamentApplicationResponse>>> getApplicationsByTournament(@PathVariable Long tournamentId) {
        List<com.sportstrust.hub.dto.TournamentApplicationResponse> responses = tournamentService.getApplicationsByTournament(tournamentId);
        return ResponseEntity.ok(ApiResponse.success(responses, "Applications retrieved successfully"));
    }

    @PutMapping("/admin/applications/{applicationId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Approve a tournament application (Admin Only)")
    public ResponseEntity<ApiResponse<com.sportstrust.hub.dto.TournamentApplicationResponse>> approveApplication(@PathVariable Long applicationId) {
        com.sportstrust.hub.dto.TournamentApplicationResponse response = tournamentService.updateApplicationStatus(applicationId, com.sportstrust.hub.entity.enums.ApplicationStatus.APPROVED);
        return ResponseEntity.ok(ApiResponse.success(response, "Application approved successfully"));
    }

    @PutMapping("/admin/applications/{applicationId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reject a tournament application (Admin Only)")
    public ResponseEntity<ApiResponse<com.sportstrust.hub.dto.TournamentApplicationResponse>> rejectApplication(@PathVariable Long applicationId) {
        com.sportstrust.hub.dto.TournamentApplicationResponse response = tournamentService.updateApplicationStatus(applicationId, com.sportstrust.hub.entity.enums.ApplicationStatus.REJECTED);
        return ResponseEntity.ok(ApiResponse.success(response, "Application rejected successfully"));
    }
}
