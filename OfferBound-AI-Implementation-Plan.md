# CareerPilot AI — Implementation Roadmap

## 1. Tech Stack Recommendation (Java Backend — Placement-Friendly)

| Layer | Suggested Choice | Notes |
|---|---|---|
| Frontend | React (Vite) + Tailwind CSS, or plain HTML/CSS/JS if time-constrained | Keep it simple if backend is your focus area for interviews |
| Backend | **Java 17+ with Spring Boot** (Spring Web, Spring Security, Spring Data JPA) | This is the framework most placement interviewers will quiz you on — good ROI |
| Database | PostgreSQL or MySQL | Use Spring Data JPA / Hibernate as the ORM |
| File Storage | Local filesystem for demo, AWS S3 for a "production-grade" touch | Store uploaded resumes (PDF/DOCX) |
| AI/LLM | OpenAI API / Anthropic API (called via Java `HttpClient` or `RestTemplate`/`WebClient`) | Resume parsing, matching, question generation, evaluation |
| Resume Parsing | Apache PDFBox (PDF), Apache POI (DOCX) | Extract raw text server-side in Java, then send to LLM for structuring |
| Auth | Spring Security + JWT (`jjwt` library) | Standard, resume-worthy pattern |
| Speech (optional) | Web Speech API (browser-side) or Whisper API | For voice-based mock interviews |
| Build Tool | Maven or Gradle | Maven is more commonly taught/expected in college placements |
| Deployment | Frontend on Vercel/Netlify; backend as a Spring Boot JAR on Render/Railway, or a college server | Docker optional but a nice-to-have for your resume |
| Async (optional) | `@Async` + Spring's `CompletableFuture`, or a simple job table with polling | Avoids needing a separate message queue for a college project |

**Why this matters for placements**: Spring Boot + Spring Security + JPA + REST APIs is close to the most commonly asked Java backend stack in interviews. Building this project gives you talking points for System Design, DBMS, and Java rounds simultaneously — not just a working app.

---

## 2. Phase-by-Phase Build Plan

### Phase 0 — Project Setup
1. Define system architecture diagram (frontend, backend, DB, AI service, file storage) — good to include in your final report/PPT.
2. Set up separate repos or folders: `/client` (React) and `/server` (Spring Boot).
3. Generate the Spring Boot project via [start.spring.io](https://start.spring.io) with dependencies: Spring Web, Spring Data JPA, Spring Security, PostgreSQL Driver, Validation, Lombok.
4. Set up project package structure: `controller`, `service`, `repository`, `entity`, `dto`, `config`, `exception`.
5. Connect to PostgreSQL locally; configure `application.properties`/`application.yml`.
6. Set up frontend scaffold with routing and base UI theme.
7. Store secrets (DB credentials, LLM API keys) in `application.properties` excluded via `.gitignore`, or use environment variables.
8. (Optional but resume-worthy) Set up a basic GitHub Actions workflow to build/test on push.

### Phase 1 — Authentication & User Management
1. Design `User` entity (id, name, email, password_hash, created_at) with Spring Data JPA.
2. Build `/auth/register` and `/auth/login` REST endpoints; issue JWTs on login using the `jjwt` (or `java-jwt`) library.
3. Configure Spring Security: `PasswordEncoder` (BCrypt), a `JwtAuthenticationFilter`, and a `SecurityFilterChain` that protects all routes except auth/public ones.
4. Add DTO-based request validation (`@Valid`, `@NotBlank`, `@Email`, etc.).
5. Add a global exception handler (`@ControllerAdvice`) for clean error responses.
6. Frontend: signup/login forms, store JWT (e.g., in memory or httpOnly cookie), attach token to API calls, protected routes.
7. (Optional) Add "Forgot Password" flow and email verification via Spring Mail.

### Phase 2 — Resume Upload & Parsing
1. Build a `MultipartFile` upload endpoint (accept PDF/DOCX, validate size/type via `@RequestParam` + custom validator).
2. Store raw file on local disk (for demo) or S3; save file reference in DB (`Resume` entity).
3. Extract raw text server-side using **Apache PDFBox** (PDF) and **Apache POI** (`XWPFDocument` for DOCX).
4. Send extracted raw text to an LLM prompt to convert into structured JSON: `{ name, contact, skills, experience[], projects[], education[], certifications }`. Use Java's `HttpClient` (or Spring's `WebClient`/`RestTemplate`) to call the LLM API, and Jackson (`ObjectMapper`) to parse the JSON response into a DTO.
5. Store structured resume data in DB — either as a `JSONB` column (Postgres) or normalized into related tables (`Skill`, `Experience`, `Project` entities linked to `Resume`).
6. Frontend: drag-and-drop upload UI + parsed-resume preview screen for user to confirm/edit extracted fields.

### Phase 3 — Job Description Input & Matching Engine
1. Build a form/textarea for job description input; store in `job_descriptions` table.
2. Design the "matching" LLM prompt: input = parsed resume JSON + JD text; output = structured JSON with:
   - matched_skills[]
   - missing_skills[]
   - strengths[]
   - weaknesses[]
   - ats_score (0–100)
   - improvement_suggestions[]
3. Build ATS scoring logic — combine rule-based checks (keyword density, formatting, section presence) with LLM judgment for a hybrid, more defensible score.
4. Create `/analyze` endpoint that orchestrates: fetch resume + JD → call LLM → parse/validate JSON response → persist result (`analysis_results` table).
5. Frontend: Analysis results page — score gauge, matched vs missing skills (chips/tags), strengths/weaknesses cards, suggestions list.

### Phase 4 — Skill Gap & Learning Recommendations
1. From `missing_skills[]`, generate a learning-path prompt: LLM suggests topics, resources, or study order.
2. Store recommendations linked to the analysis record.
3. Frontend: "What to learn next" section with prioritized skill list.

### Phase 5 — AI Mock Interview Engine (core differentiator)
1. Design interview session schema: `interview_sessions` (id, user_id, resume_id, jd_id, status, created_at), `interview_messages` (session_id, role, content, timestamp), `interview_evaluations`.
2. Build the **question-generation prompt**: context = resume JSON + JD + missing_skills + prior Q&A history → generates next question (technical/behavioral/project/HR), with logic to vary question type.
3. Implement conversational loop:
   - User answers → append to session history.
   - Backend sends full context + last answer to LLM → LLM decides: ask follow-up (harder/deeper) or move to a new topic.
   - Track a running difficulty/adaptivity signal (e.g., simple heuristic: strong answer → increase depth; weak answer → clarify or simplify).
4. Support both text input and (optionally) speech-to-text for spoken answers.
5. Build endpoints: `POST /interview/start`, `POST /interview/answer`, `POST /interview/end`.
6. Frontend: chat-style interview UI with typing indicator, question counter, optional timer per question, mic button if voice-enabled.

### Phase 6 — Interview Evaluation & Reporting
1. At `interview/end`, send full transcript to an evaluation LLM prompt that scores:
   - Technical knowledge
   - Communication clarity
   - Problem-solving ability
   - Overall performance
2. Output structured JSON: scores per category, strengths, weaknesses, improvement suggestions, recommended study topics.
3. Persist to `interview_evaluations`.
4. Build downloadable report (PDF) — use **iText** or **Apache PDFBox** on the backend to render the evaluation into a styled PDF, served via a /reports/{id}/download endpoint.
5. Frontend: Report page — score breakdown (charts), narrative feedback, "Download PDF" button.

### Phase 7 — Interview History Dashboard
1. Build `GET /interviews` endpoint returning past sessions with summary scores.
2. Frontend dashboard: list/table of past interviews with date, JD title, overall score, link to full report.
3. Add trend visualization (score over time) if user has multiple attempts.

### Phase 8 — Polish, Testing, and Hardening
1. Add input validation and error handling across all AI-calling endpoints (LLMs can return malformed JSON — always validate/retry).
2. Add rate limiting on AI endpoints to control cost and abuse.
3. Write unit tests (backend logic) and integration tests (upload → analyze → interview → report flow).
4. Add loading states, skeletons, and graceful error messages on frontend for all async AI calls (these take a few seconds).
5. Accessibility pass (keyboard nav, screen-reader labels) and responsive design check.

### Phase 9 — Deployment
1. Containerize backend (Docker) for portability.
2. Set up production Postgres (managed service e.g. Supabase/RDS).
3. Deploy frontend (Vercel) and backend (Render/Railway/AWS/GCP).
4. Configure environment secrets in hosting provider.
5. Set up monitoring/logging (e.g., Sentry) and basic uptime checks.
6. Set up cost monitoring for LLM API usage (this is the main variable cost driver).

---

## 3. Key Technical Risks to Plan For Early
- **LLM output reliability**: always request structured JSON output and validate/parse defensively (retry on malformed responses).
- **Cost control**: mock interviews involve many LLM calls per session — consider capping question count, using cheaper models for follow-up logic, and caching where possible.
- **File parsing edge cases**: scanned/image-based PDFs won't extract text well — consider OCR fallback or clear user messaging.
- **Latency**: interview conversation needs to feel responsive — stream LLM responses to the frontend rather than waiting for full completion.
- **Data privacy**: resumes contain PII — encrypt at rest, restrict access, and have a clear data retention/deletion policy.

## 4. Suggested Build Order (MVP-first)
1. Auth → 2. Resume upload/parsing → 3. JD input + matching/ATS score → 4. Basic mock interview (fixed flow first, then adaptive) → 5. Evaluation report → 6. Dashboard/history → 7. Polish + deploy.

This lets you get a working end-to-end demo (upload → analysis → interview → report) before investing in adaptivity, voice input, and history analytics.

## 5. Placement-Interview Angle
Since this is for placements, treat the build itself as interview prep, not just the deliverable:
- Be ready to explain your **entity relationships** (draw the ER diagram) and why you chose JPA/Hibernate mappings the way you did.
- Know the **Spring Security filter chain** flow end-to-end (this is a very common "explain how JWT auth works in your project" question).
- Be able to justify **REST API design choices** (status codes, DTO vs entity exposure, pagination on the history endpoint).
- Have a clear answer for **"why Spring Boot"** and one architectural trade-off you made (e.g., JSONB vs normalized tables for parsed resume data).
- Practice explaining the **LLM integration** simply — interviewers may not care about prompt engineering, but they will ask how you handled unreliable third-party API responses (timeouts, retries, validation).
