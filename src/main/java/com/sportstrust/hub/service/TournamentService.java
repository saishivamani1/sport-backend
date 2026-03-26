package com.sportstrust.hub.service;

import com.sportstrust.hub.dto.TournamentApplicationResponse;
import com.sportstrust.hub.dto.TournamentCreateRequest;
import com.sportstrust.hub.dto.TournamentResponse;
import com.sportstrust.hub.entity.Tournament;
import com.sportstrust.hub.entity.TournamentApplication;
import com.sportstrust.hub.entity.User;
import com.sportstrust.hub.entity.enums.ApplicationStatus;
import com.sportstrust.hub.exception.CustomException;
import com.sportstrust.hub.mapper.EntityMapper;
import com.sportstrust.hub.repository.TournamentApplicationRepository;
import com.sportstrust.hub.repository.TournamentRepository;
import com.sportstrust.hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final EntityMapper entityMapper;

    @Transactional
    public TournamentResponse createTournament(TournamentCreateRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new CustomException("Start date cannot be after end date", HttpStatus.BAD_REQUEST);
        }

        Tournament tournament = Tournament.builder()
                .name(request.getName())
                .sport(request.getSport())
                .location(request.getLocation())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .registrationDeadline(request.getRegistrationDeadline())
                .description(request.getDescription())
                .maxParticipants(request.getMaxParticipants())
                .prizePool(request.getPrizePool())
                .build();

        Tournament saved = tournamentRepository.save(tournament);
        return entityMapper.toTournamentResponse(saved);
    }

    public List<TournamentResponse> getAllTournaments() {
        return entityMapper.toTournamentResponseList(tournamentRepository.findAll());
    }

    public TournamentResponse getTournamentById(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new CustomException("Tournament not found", HttpStatus.NOT_FOUND));
        return entityMapper.toTournamentResponse(tournament);
    }

    @Transactional
    public TournamentApplicationResponse applyForTournament(Long userId, Long tournamentId) {
        User athlete = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new CustomException("Tournament not found", HttpStatus.NOT_FOUND));

        if (LocalDate.now().isAfter(tournament.getRegistrationDeadline())) {
            throw new CustomException("Registration deadline has passed", HttpStatus.BAD_REQUEST);
        }

        if (applicationRepository.findByAthleteAndTournament(athlete, tournament).isPresent()) {
            throw new CustomException("Already applied for this tournament", HttpStatus.BAD_REQUEST);
        }

        TournamentApplication application = TournamentApplication.builder()
                .athlete(athlete)
                .tournament(tournament)
                .status(ApplicationStatus.PENDING)
                .build();

        TournamentApplication saved = applicationRepository.save(application);
        return entityMapper.toTournamentApplicationResponse(saved);
    }

    public List<com.sportstrust.hub.dto.TournamentApplicationResponse> getAllApplications() {
        return entityMapper.toTournamentApplicationResponseList(applicationRepository.findAll());
    }

    public List<com.sportstrust.hub.dto.TournamentApplicationResponse> getApplicationsByTournament(Long tournamentId) {
        com.sportstrust.hub.entity.Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new com.sportstrust.hub.exception.CustomException("Tournament not found", org.springframework.http.HttpStatus.NOT_FOUND));
        return entityMapper.toTournamentApplicationResponseList(applicationRepository.findByTournament(tournament));
    }

    @org.springframework.transaction.annotation.Transactional
    public com.sportstrust.hub.dto.TournamentApplicationResponse updateApplicationStatus(Long applicationId, com.sportstrust.hub.entity.enums.ApplicationStatus status) {
        com.sportstrust.hub.entity.TournamentApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new com.sportstrust.hub.exception.CustomException("Application not found", org.springframework.http.HttpStatus.NOT_FOUND));
        
        application.setStatus(status);
        com.sportstrust.hub.entity.TournamentApplication saved = applicationRepository.save(application);
        return entityMapper.toTournamentApplicationResponse(saved);
    }
}
