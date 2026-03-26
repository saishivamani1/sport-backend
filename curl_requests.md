# Test cURL Requests

These commands assume your Spring Boot backend is running on `http://localhost:8080`.

## 1. Authentication

### Register as an Athlete
```bash
curl -X POST http://localhost:8080/auth/register \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Alex Johnson",
           "email": "alex@example.com",
           "password": "password123",
           "role": "ATHLETE",
           "sport": "Football",
           "position": "Striker",
           "experienceYears": 6
         }'
```

### Register as an Admin
```bash
curl -X POST http://localhost:8080/auth/register \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Admin User",
           "email": "admin@example.com",
           "password": "adminpassword",
           "role": "ADMIN"
         }'
```

### Login (Get Token)
```bash
curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{
           "email": "alex@example.com",
           "password": "password123"
         }'
```
> **Note**: Copy the `accessToken` from the response to use in the subsequent requests. Replace `<YOUR_TOKEN>` below.

---

## 2. Profile Verification

### Submit ID Proof (Athlete)
```bash
curl -X POST http://localhost:8080/verification/submit-id \
     -H "Authorization: Bearer <YOUR_TOKEN>" \
     -H "Content-Type: application/json" \
     -d '{
           "idProofUrl": "https://trusted-bucket.s3.amazonaws.com/ids/alex_johnson_id.pdf"
         }'
```

### Get Profile Status (Athlete)
```bash
curl -X GET http://localhost:8080/verification/status \
     -H "Authorization: Bearer <YOUR_TOKEN>"
```

### Approve Athlete Verification (Admin Token Required)
```bash
curl -X PUT http://localhost:8080/admin/verifications/1/approve \
     -H "Authorization: Bearer <ADMIN_TOKEN>"
```

---

## 3. Tournaments

### Create Tournament (Admin Token Required)
```bash
curl -X POST http://localhost:8080/tournaments \
     -H "Authorization: Bearer <ADMIN_TOKEN>" \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Global Football Cup 2026",
           "sport": "Football",
           "location": "London, UK",
           "startDate": "2026-08-01",
           "endDate": "2026-08-30",
           "registrationDeadline": "2026-07-01"
         }'
```

### Get All Tournaments (Public)
```bash
curl -X GET http://localhost:8080/tournaments
```

### Apply for Tournament (Athlete Token Required)
```bash
curl -X POST http://localhost:8080/tournaments/1/apply \
     -H "Authorization: Bearer <YOUR_TOKEN>"
```

---

## 4. Achievements

### Submit Achievement (Verified Athlete Only)
```bash
curl -X POST http://localhost:8080/posts/submit \
     -H "Authorization: Bearer <YOUR_TOKEN>" \
     -H "Content-Type: application/json" \
     -d '{
           "title": "Top Scorer of the Year",
           "description": "Awarded the golden boot for scoring the most goals.",
           "proofUrl": "https://example.com/golden_boot.jpg"
         }'
```

### Approve Achievement Post (Admin Token Required)
```bash
curl -X PUT http://localhost:8080/admin/posts/1/approve \
     -H "Authorization: Bearer <ADMIN_TOKEN>"
```

### Get Public Feed (Approved Posts Only)
```bash
curl -X GET http://localhost:8080/posts/feed \
     -H "Authorization: Bearer <YOUR_TOKEN>"
```

---

## 5. Discovery

### Search for Athletes (Sponsor/Scout/Coach Token Required)
```bash
curl -X GET "http://localhost:8080/athletes?sport=Football&minCredibility=10.0" \
     -H "Authorization: Bearer <SCOUT_TOKEN>"
```

### Get Athlete Details
```bash
curl -X GET http://localhost:8080/athletes/1 \
     -H "Authorization: Bearer <SCOUT_TOKEN>"
```
