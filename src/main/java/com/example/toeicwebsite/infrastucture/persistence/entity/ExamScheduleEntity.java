package com.example.toeicwebsite.infrastucture.persistence.entity;

import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "exam_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_id", nullable = false, unique = true)
    private UUID businessId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private ExamEntity exam;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamMode mode;

    @Column(name = "open_at")
    private LocalDateTime openAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    private Integer partNumber;
}
