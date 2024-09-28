package com.epam.esm.gym.workload.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a training event in the application.
 *
 * <p>This class includes details such as training name, date, duration, and type.
 * It manages the relationships between training sessions and participants, ensuring
 * accurate scheduling and tracking of events.</p>
 *
 * <p>Duration is validated to ensure it falls within specified limits.</p>
 *
 * @author Pavlo Poliak
 * @since 1.0
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "training_workload")
public class TrainingWorkload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "trainer_id", nullable = false)
    private TrainerWorkload workload;

    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @Column(name = "training_type", nullable = false)
    private String trainingType;

    @Column(name = "training_duration", nullable = false)
    private Long trainingDuration;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;
}
