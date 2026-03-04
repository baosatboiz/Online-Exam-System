TOEIC Exam Backend: Core Technical Problems
1. Concurrent Submission
   •	Challenges:
   o	Multiple users submit at the same time.
   o	Same user may submit multiple times.
   •	Tech Focus:
   o	Idempotent submit logic.
   o	Server-side authority.
   o	Transactional submit handling.
2. Time Boundary Enforcement
   •	Challenges:
   o	Manual submit and auto-expire may occur simultaneously.
   o	Client time is unreliable.
   •	Tech Focus:
   o	Server-side time validation.
   o	Deterministic state transition: $IN\_PROGRESS \to SUBMITTED \mid EXPIRED$
3. Concurrency Control (Read & Write)
   •	Challenges:
   o	Reads happen while submissions (writes) are in progress.
   o	High contention near exam deadline.
   •	Tech Focus:
   o	Transaction boundaries.
   o	Isolation level awareness (MVCC).
   o	Preventing lost updates without blocking reads.
4. Deterministic Scoring
   •	Challenges:
   o	Score must always be reproducible.
   o	No recalculation inconsistency.
   •	Tech Focus:
   o	Immutable submission snapshot.
   o	Atomic score calculation.
5. Read-Heavy Data Modeling
   •	Challenges:
   o	Exam → Question → Choice hierarchy.
   o	Many reads, few writes.
   •	Tech Focus:
   o	Proper relationships.
   o	Avoid N+1 queries.
   o	Read-optimized queries.
6. Layer Boundary Discipline
   •	Challenges:
   o	Business logic must not depend on persistence.
   o	Infrastructure must not leak into domain.
   •	Tech Focus:
   o	Domain purity.
   o	Application-layer orchestration.

