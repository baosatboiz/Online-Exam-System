TOEIC Practice Backend Day 1 – Backend Task Assignment
This document defines Day 1 responsibilities for two backend developers working in parallel.
General Rules
•	Branch naming:
o	be-a/*
o	be-b/*
•	No direct commits to main
•	Keep commits small and focused
•	Do not modify another developer’s layer without agreement
________________________________________
Backend A – Infrastructure, Persistence & Data Seeding Goal: Provide a working database foundation with initial data.
Responsibilities
•	Database Migration (Flyway)
o	Create initial schema: exam, question, choice
o	Define: primary keys, foreign keys, basic indexes
•	JPA Persistence Layer
o	JPA entities under: infrastructure/persistence/jpa/entity
o	Spring Data JPA repositories under: infrastructure/persistence/jpa/repository
o	No business logic in entities
•	Domain Repository Implementations
o	Implement domain repository interfaces
o	Handle mapping between domain models and JPA entities
•	Data Seeding
o	Insert sample data using Flyway SQL migrations
o	Seed: at least 1 exam, multiple questions and choices
o	Purpose:
	Application starts with usable data
	Backend can be tested without frontend
Deliverables
•	Flyway migration V1 (schema)
•	Flyway migration V2 (seed data)
•	JPA entities and repositories
•	Domain repository implementations
________________________________________
Backend B – Domain & Application Layer Goal: Define core business logic and application use cases, independent from database and framework.
Responsibilities
•	Domain Model
o	Create domain packages: domain/exam, domain/question
o	Define entities and business rules
o	No JPA or Spring annotations
•	Domain Repository Interfaces
o	Define repository contracts in domain layer
o	Express business intent, not persistence details
•	Application Use Cases
o	Create use cases under: application/exam
o	Examples: GetExamDetail, ListExams
o	Application layer depends only on domain interfaces
•	Application DTOs
o	Request / response models for use cases
o	No persistence or web annotations
Deliverables
•	Domain model compiles standalone
•	Application layer compiles without infrastructure
•	Use cases can read seeded data via repositories
________________________________________
Coordination Rules
•	Backend B: * Uses domain repository interfaces only
o	Does not access JPA or SQL
•	Backend A: * Controls database schema and data
o	Does not add business logic
•	Integration happens after Day 1

Allowed Commit Types
Type	Description
feat	Introduces a new feature
fix	Fixes a bug
refactor	Code change without changing behavior
docs	Documentation only
test	Adding or updating tests
chore	Tooling, configuration, dependencies