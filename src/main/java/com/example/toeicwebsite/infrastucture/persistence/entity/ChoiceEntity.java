package com.example.toeicwebsite.infrastucture.persistence.entity;

import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "choice", uniqueConstraints = {
        @UniqueConstraint(name = "uq_question_label", columnNames = {"question_id", "label"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChoiceKey label;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;
}
