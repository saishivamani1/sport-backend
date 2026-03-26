-- Database Schema for Integrated Hub for Talent, Unity, Recruitment & Sports
-- Target: MySQL

CREATE DATABASE IF NOT EXISTS sports_hub;
USE sports_hub;

-- Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL, -- ADMIN, ATHLETE, COACH, SCOUT, SPONSOR
    account_status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, LOCKED, SUSPENDED
    failed_login_attempts INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Athlete Profiles Table
CREATE TABLE athlete_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    sport VARCHAR(100) NOT NULL,
    position VARCHAR(100),
    experience_years INT DEFAULT 0,
    verification_status VARCHAR(50) NOT NULL DEFAULT 'BASIC', -- BASIC, PENDING_VERIFICATION, VERIFIED, REJECTED
    id_proof_url VARCHAR(500),
    credibility_score DOUBLE DEFAULT 0.0,
    total_tournaments INT DEFAULT 0,
    total_wins INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tournaments Table
CREATE TABLE tournaments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    sport VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    registration_deadline DATE NOT NULL
);

-- Tournament Applications Table
CREATE TABLE tournament_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    athlete_id BIGINT NOT NULL,
    tournament_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (athlete_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id) ON DELETE CASCADE,
    UNIQUE KEY athlete_tournament_unique (athlete_id, tournament_id)
);

-- Achievement Posts Table
CREATE TABLE achievement_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    athlete_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    proof_url VARCHAR(500),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING_REVIEW', -- PENDING_REVIEW, APPROVED, REJECTED
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_by BIGINT,
    reviewed_at TIMESTAMP NULL,
    FOREIGN KEY (athlete_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewed_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Moderation Logs Table
CREATE TABLE moderation_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    admin_id BIGINT NOT NULL,
    action_type VARCHAR(100) NOT NULL, -- APPROVE_ID, REJECT_ID, APPROVE_POST, REJECT_POST, SUSPEND_ACCOUNT
    target_id BIGINT NOT NULL,
    description TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE CASCADE
);
