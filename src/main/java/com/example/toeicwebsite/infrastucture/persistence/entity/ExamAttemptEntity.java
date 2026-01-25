package com.example.toeicwebsite.infrastucture.persistence.entity;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "exam_attempts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamAttemptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_id", nullable = false, unique = true)
    private UUID businessId;

    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_schedule_id", nullable = false)
    private ExamScheduleEntity examSchedule;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "must_finished_at", nullable = false)
    private LocalDateTime mustFinishedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamStatus status;
}
