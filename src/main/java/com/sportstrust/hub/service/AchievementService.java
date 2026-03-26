package com.sportstrust.hub.service;

import com.sportstrust.hub.dto.AchievementResponse;
import com.sportstrust.hub.dto.AchievementSubmitRequest;
import com.sportstrust.hub.entity.AchievementPost;
import com.sportstrust.hub.entity.AthleteProfile;
import com.sportstrust.hub.entity.ModerationLog;
import com.sportstrust.hub.entity.User;
import com.sportstrust.hub.entity.enums.ActionType;
import com.sportstrust.hub.entity.enums.PostStatus;
import com.sportstrust.hub.entity.enums.VerificationStatus;
import com.sportstrust.hub.exception.CustomException;
import com.sportstrust.hub.mapper.EntityMapper;
import com.sportstrust.hub.repository.AchievementPostRepository;
import com.sportstrust.hub.repository.AthleteProfileRepository;
import com.sportstrust.hub.repository.ModerationLogRepository;
import com.sportstrust.hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementPostRepository postRepository;
    private final UserRepository userRepository;
    private final AthleteProfileRepository athleteProfileRepository;
    private final ModerationLogRepository moderationLogRepository;
    private final EntityMapper entityMapper;

    @Transactional
    public AchievementResponse submitPost(Long athleteId, AchievementSubmitRequest request) {
        @SuppressWarnings("null")
        User athlete = userRepository.findById(athleteId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        AthleteProfile profile = athleteProfileRepository.findByUser(athlete)
                .orElseThrow(() -> new CustomException("Profile not found", HttpStatus.NOT_FOUND));

        if (profile.getVerificationStatus() != VerificationStatus.VERIFIED) {
            throw new CustomException("Only verified athletes can submit achievement posts", HttpStatus.FORBIDDEN);
        }

        AchievementPost post = AchievementPost.builder()
                .athlete(athlete)
                .title(request.getTitle())
                .description(request.getDescription())
                .proofUrl(request.getProofUrl())
                .status(PostStatus.PENDING_REVIEW)
                .build();

        @SuppressWarnings("null")
        AchievementPost saved = postRepository.save(post);
        return entityMapper.toAchievementResponse(saved);
    }

    public List<AchievementResponse> getFeed() {
        return entityMapper.toAchievementResponseList(
                postRepository.findByStatusOrderBySubmittedAtDesc(PostStatus.APPROVED)
        );
    }

    public List<AchievementResponse> getMyPosts(Long athleteId) {
        @SuppressWarnings("null")
        User athlete = userRepository.findById(athleteId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        return entityMapper.toAchievementResponseList(postRepository.findByAthlete(athlete));
    }

    public List<AchievementResponse> getPendingPosts() {
        return entityMapper.toAchievementResponseList(
                postRepository.findByStatusOrderBySubmittedAtDesc(PostStatus.PENDING_REVIEW)
        );
    }

    @SuppressWarnings("null")
@Transactional
    public void reviewPost(Long adminId, Long postId, boolean isApproved, String reason) {
        @SuppressWarnings("null")
        AchievementPost post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("Post not found", HttpStatus.NOT_FOUND));

        @SuppressWarnings("null")
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new CustomException("Admin not found", HttpStatus.NOT_FOUND));

        if (post.getStatus() != PostStatus.PENDING_REVIEW) {
            throw new CustomException("Post is already reviewed", HttpStatus.BAD_REQUEST);
        }

        post.setStatus(isApproved ? PostStatus.APPROVED : PostStatus.REJECTED);
        post.setReviewedBy(admin);
        post.setReviewedAt(LocalDateTime.now());

        if (isApproved) {
            // Update credibility score
            AthleteProfile profile = athleteProfileRepository.findByUser(post.getAthlete())
                    .orElseThrow(() -> new CustomException("Athlete profile not found", HttpStatus.NOT_FOUND));
            
            Double currentScore = profile.getCredibilityScore();
            profile.setCredibilityScore((currentScore != null ? currentScore : 0.0) + 10.0); // +10 points for approved achievement
            athleteProfileRepository.save(profile);
        }

        postRepository.save(post);

        ActionType action = isApproved ? ActionType.APPROVE_POST : ActionType.REJECT_POST;
        ModerationLog log = ModerationLog.builder()
                .admin(admin)
                .actionType(action)
                .targetId(postId)
                .description("Post " + (isApproved ? "approved" : "rejected. Reason: " + reason))
                .build();
        moderationLogRepository.save(log);
    }
}
