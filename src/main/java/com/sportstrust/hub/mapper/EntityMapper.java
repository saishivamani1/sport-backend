package com.sportstrust.hub.mapper;

import com.sportstrust.hub.dto.*;
import com.sportstrust.hub.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    ProfileView toProfileView(AthleteProfile profile);

    TournamentResponse toTournamentResponse(Tournament tournament);
    List<TournamentResponse> toTournamentResponseList(List<Tournament> tournaments);

    @Mapping(target = "athleteId", source = "athlete.id")
    @Mapping(target = "athleteName", source = "athlete.name")
    @Mapping(target = "tournamentId", source = "tournament.id")
    @Mapping(target = "tournamentName", source = "tournament.name")
    TournamentApplicationResponse toTournamentApplicationResponse(TournamentApplication application);
    List<TournamentApplicationResponse> toTournamentApplicationResponseList(List<TournamentApplication> applications);

    @Mapping(target = "athleteId", source = "athlete.id")
    @Mapping(target = "athleteName", source = "athlete.name")
    AchievementResponse toAchievementResponse(AchievementPost post);
    List<AchievementResponse> toAchievementResponseList(List<AchievementPost> posts);
}
