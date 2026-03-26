package com.sportstrust.hub.repository;

import com.sportstrust.hub.entity.AchievementPost;
import com.sportstrust.hub.entity.User;
import com.sportstrust.hub.entity.enums.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementPostRepository extends JpaRepository<AchievementPost, Long> {
    List<AchievementPost> findByAthlete(User athlete);
    List<AchievementPost> findByStatusOrderBySubmittedAtDesc(PostStatus status);
    List<AchievementPost> findByAthleteAndStatus(User athlete, PostStatus status);
}
