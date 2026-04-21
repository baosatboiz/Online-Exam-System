# TOEIC Online Exam System

A specialized platform for TOEIC practice and examination, focusing on high-performance scoring and strict time management.

---

## 🚀 How to Run

Follow these steps to deploy the system locally using Docker:

1.  **Initialize Containers**: Execute the command below in the root directory:
    ```bash
    docker-compose up -d
    ```
2.  **Startup Latency**: Please wait approximately **30 seconds** for the Backend service (Spring Boot) to fully initialize. The system requires time to set up the JPA context and execute database migrations.
3.  **Access Points**:
    * **User Interface**: [http://localhost:3000](http://localhost:3000)

---

## 🧠 Core Technical Problems & Solutions

### 1. Concurrent Submission
* **Challenges**: Multiple users submitting simultaneously or a single user triggering multiple submissions due to network latency.
* **Tech Focus**: 
    * Implementation of **Idempotent** submission logic.
    * Enforcement of server-side authority for all grade calculations.
    * Atomic transactional handling for submission records.

### 2. Time Boundary Enforcement
* **Challenges**: Discrepancies between client and server time; handling simultaneous manual submissions and automatic expirations.
* **Tech Focus**: 
    * Strict server-side time validation.
    * Deterministic state transition model: 
      $$IN\_PROGRESS \to SUBMITTED \mid EXPIRED$$

### 3. Concurrency Control (Read & Write)
* **Challenges**: Maintaining performance during high contention periods (e.g., just before an exam deadline) where reads and writes overlap.
* **Tech Focus**: 
    * Defined transaction boundaries for consistent data states.
    * Leveraging **MVCC (Multi-Version Concurrency Control)** for non-blocking reads.
    * Preventing lost updates during high-concurrency write phases.

### 4. Deterministic Scoring
* **Challenges**: Ensuring scores are always reproducible and consistent, regardless of future changes to the question bank.
* **Tech Focus**: 
    * Capturing **Immutable snapshots** of user submissions.
    * Using atomic calculation logic to ensure scoring accuracy.

### 5. Read-Heavy Data Modeling
* **Challenges**: Optimizing the hierarchical relationship (Exam → Question → Choice) to handle high read volumes.
* **Tech Focus**: 
    * Establishing proper database relationships (One-to-Many/Many-to-One).
    * Eliminating **N+1 query** problems using Fetch Joins and EntityGraphs.
    * Implementing read-optimized query structures.

### 6. Layer Boundary Discipline
* **Challenges**: Preventing business logic from leaking into persistence or infrastructure layers.
* **Tech Focus**: 
    * Maintaining **Domain Purity** (Core logic remains framework-independent).
    * Orchestrating system behavior through a clean application-layer architecture.
