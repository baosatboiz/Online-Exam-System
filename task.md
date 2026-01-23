🎯 TOEIC Practice Backend – Day 2 Task Assignment
This document outlines the parallel development tasks for the MVP, ensuring a clear separation between content infrastructure and the core exam execution flow.

🛠️ Developer A: Content & Setup
Goal: Ensure the system has "exams to take" and "active windows to enter".

UC 1: CreateExam (Create Exam Template)

Mission: Receive the exam structure (metadata, parts) and persist it into the system.

Output: A complete exam set ready to be linked to a schedule.

UC 2: GetExamDetail (View Exam Details)

Mission: Retrieve the exam structure (number of questions per part, total duration) for the User's preview screen.

Output: Metadata used by the Next.js frontend before the user clicks "Start".

UC 3: CreateExamSchedule (Open Exam Session)

Mission: Attach an ExamId to a specific time window (openAt, endAt) and select the mode (REAL or PRACTICE).

Output: An exam schedule appearing on the student's dashboard.

🚀 Developer B: Flow & Scoring
Goal: Handle the "heart" of the system—the actual testing experience for the user.

UC 5: StartExamAttempt (Start Exam)

Mission: Validate schedule conditions and initialize the attempt. The primary focus is calculating the mandatory submission time (mustFinishedAt) based on the start time and the schedule's closing gate.

Output: An ExamAttemptId and a real-time countdown for the student.

UC 6: GetAttemptQuestions (Retrieve Questions)

Mission: Retrieve all questions, images, and audio files from the corresponding exam template based on the ExamAttemptId.

Output: A list of questions sorted in correct order (1–200) for the testing interface.

UC 7: SubmitAnswer (Save Answer)

Mission: Save the user's choice (A, B, C, D) incrementally as they are selected, while continuously verifying that the attempt is still within the allowed time.

Output: A "saved" status to ensure a seamless user experience.

UC 8: Finish & GetResult (Submit & Score)

Mission: Terminate the attempt and lock it to prevent further changes. Execute the scoring algorithm to cross-reference answers with the correct keys for Listening and Reading sections.

Output: A detailed score report (correct/incorrect counts) and a total TOEIC score (0–990).

🛠️ Technical Implementation Notes
Persistence: All data must be persisted in PostgreSQL using Spring Data JPA.

Domain Integrity: Business rules (scoring and time validation) must remain within the Domain Layer and remain independent of the web framework.

Allowed Commit Types
Type	Description
feat	Introduces a new feature
fix	Fixes a bug
refactor	Code change without changing behavior
docs	Documentation only
test	Adding or updating tests
chore	Tooling, configuration, dependencies