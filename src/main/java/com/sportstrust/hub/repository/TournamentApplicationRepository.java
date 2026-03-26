package com.sportstrust.hub.repository;

import com.sportstrust.hub.entity.Tournament;
import com.sportstrust.hub.entity.TournamentApplication;
import com.sportstrust.hub.entity.User;
import com.sportstrust.hub.entity.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentApplicationRepository extends JpaRepository<TournamentApplication, Long> {
    List<TournamentApplication> findByAthlete(User athlete);
    List<TournamentApplication> findByTournament(Tournament tournament);
    Optional<TournamentApplication> findByAthleteAndTournament(User athlete, Tournament tournament);
    List<TournamentApplication> findByStatus(ApplicationStatus status);
}
