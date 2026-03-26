package com.sportstrust.hub.repository;

import com.sportstrust.hub.entity.ModerationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModerationLogRepository extends JpaRepository<ModerationLog, Long> {
}
