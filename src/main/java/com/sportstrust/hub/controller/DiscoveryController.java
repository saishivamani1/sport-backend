package com.sportstrust.hub.controller;

import com.sportstrust.hub.dto.ApiResponse;
import com.sportstrust.hub.dto.ProfileView;
import com.sportstrust.hub.service.DiscoveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/athletes")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Athlete Discovery", description = "Endpoints for scouts and sponsors to discover verified athletes")
public class DiscoveryController {

    private final DiscoveryService discoveryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SCOUT', 'SPONSOR', 'COACH', 'ADMIN')")
    @Operation(summary = "Search for verified athletes")
    public ResponseEntity<ApiResponse<List<ProfileView>>> searchAthletes(
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) Double minCredibility) {
        List<ProfileView> athletes = discoveryService.searchAthletes(sport, minCredibility);
        return ResponseEntity.ok(ApiResponse.success(athletes, "Athletes retrieved successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SCOUT', 'SPONSOR', 'COACH', 'ADMIN')")
    @Operation(summary = "Get detailed profile of an athlete")
    public ResponseEntity<ApiResponse<ProfileView>> getAthleteDetails(@PathVariable Long id) {
        ProfileView profile = discoveryService.getAthleteDetails(id);
        return ResponseEntity.ok(ApiResponse.success(profile, "Athlete details retrieved successfully"));
    }
}
