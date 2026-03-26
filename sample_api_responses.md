# Sample API Responses

## Authentication

### POST `/auth/register` (Athlete)
**Status:** `201 Created`
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI... (JWT)",
    "refreshToken": "eyJhbGciOiJIUzI... (JWT)",
    "userId": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "role": "ATHLETE"
  },
  "timestamp": "2026-03-12T11:50:00.000"
}
```

### POST `/auth/login`
**Status:** `200 OK`
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI...",
    "refreshToken": "eyJhbGciOiJIUzI...",
    "userId": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "role": "ATHLETE"
  },
  "timestamp": "2026-03-12T11:52:00.000"
}
```

---

## Profile & Verification

### POST `/verification/submit-id` (Athlete)
**Status:** `200 OK`
```json
{
  "success": true,
  "message": "ID proof submitted successfully. Waiting for admin approval.",
  "data": null,
  "timestamp": "2026-03-12T11:53:00.000"
}
```

### GET `/verification/status`
**Status:** `200 OK`
```json
{
  "success": true,
  "message": "Profile retrieved successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "sport": "Basketball",
    "position": "Point Guard",
    "experienceYears": 5,
    "verificationStatus": "PENDING_VERIFICATION",
    "credibilityScore": 0.0,
    "totalTournaments": 0,
    "totalWins": 0
  },
  "timestamp": "2026-03-12T11:54:00.000"
}
```

---

## Tournaments

### GET `/tournaments`
**Status:** `200 OK`
```json
{
  "success": true,
  "message": "Tournaments retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "National Summer League",
      "sport": "Basketball",
      "location": "New York, USA",
      "startDate": "2026-07-01",
      "endDate": "2026-07-15",
      "registrationDeadline": "2026-06-15"
    }
  ],
  "timestamp": "2026-03-12T11:55:00.000"
}
```

### POST `/tournaments/1/apply` (Athlete)
**Status:** `200 OK`
```json
{
  "success": true,
  "message": "Applied for tournament successfully",
  "data": {
    "id": 1,
    "athleteId": 1,
    "tournamentId": 1,
    "tournamentName": "National Summer League",
    "status": "PENDING",
    "appliedAt": "2026-03-12T11:56:00.000"
  },
  "timestamp": "2026-03-12T11:56:00.000"
}
```

---

## Achievements

### POST `/posts/submit`
**Status:** `201 Created`
```json
{
  "success": true,
  "message": "Post submitted successfully. Waiting for review.",
  "data": {
    "id": 1,
    "athleteId": 1,
    "athleteName": "John Doe",
    "title": "State Championship MVP",
    "description": "Won the MVP award at the State Championship 2025.",
    "proofUrl": "https://example.com/certificate.pdf",
    "status": "PENDING_REVIEW",
    "submittedAt": "2026-03-12T11:57:00.000"
  },
  "timestamp": "2026-03-12T11:57:00.000"
}
```

### GET `/posts/feed`
**Status:** `200 OK`
```json
{
  "success": true,
  "message": "Feed retrieved successfully",
  "data": [
    {
      "id": 5,
      "athleteId": 2,
      "athleteName": "Jane Smith",
      "title": "National Gold Medalist",
      "description": "Secured first place in 100m sprint.",
      "proofUrl": "https://example.com/gold-medal",
      "status": "APPROVED",
      "submittedAt": "2026-03-10T10:00:00.000"
    }
  ],
  "timestamp": "2026-03-12T11:58:00.000"
}
```

---

## Admin Operations

### PUT `/admin/verifications/1/approve`
**Status:** `200 OK`
```json
{
  "success": true,
  "message": "Verification approved successfully",
  "data": null,
  "timestamp": "2026-03-12T11:59:00.000"
}
```

### PUT `/admin/posts/1/reject?reason=Fake%20Certificate`
**Status:** `200 OK`
```json
{
  "success": true,
  "message": "Post rejected successfully",
  "data": null,
  "timestamp": "2026-03-12T12:00:00.000"
}
```
