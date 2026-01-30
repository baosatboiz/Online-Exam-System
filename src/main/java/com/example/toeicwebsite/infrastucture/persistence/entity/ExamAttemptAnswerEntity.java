package com.example.toeicwebsite.infrastucture.persistence.entity;

import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "exam_attempt_answer",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"attempt_id", "question_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamAttemptAnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChoiceKey choiceKey;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @ManyToOne
    @JoinColumn(name = "attempt_id", nullable = false)
    private ExamAttemptEntity examAttempt;
}
