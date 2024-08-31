package com.epam.esm.gym.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "training_type")
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialization trainingType;

    @Builder.Default
    @OneToMany(mappedBy = "type")
    private Set<Training> trainings = new HashSet<>();

    @OneToMany(mappedBy = "specialization")
    private Set<Trainer> trainers = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingType that = (TrainingType) o;
        return Objects.equals(id, that.id) && trainingType == that.trainingType && Objects.equals(trainings, that.trainings) && Objects.equals(trainers, that.trainers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trainingType, trainings, trainers);
    }
}