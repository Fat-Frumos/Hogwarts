package com.epam.esm.gym.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FetchType;
import jakarta.persistence.FieldResult;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a trainee participating in training sessions.
 *
 * <p>This class holds information about the trainee, including their user
 * profile, assigned sessions, and performance data. It is used to manage
 * trainee-specific details within the training application.</p>
 *
 * @author Pavlo Poliak
 * @since 1.0
 */
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trainee")
@SqlResultSetMapping(
        name = "TraineeMapping",
        entities = @EntityResult(entityClass = Trainee.class, fields = {
                @FieldResult(name = "id", column = "id"),
                @FieldResult(name = "dateOfBirth", column = "date_of_birth"),
                @FieldResult(name = "address", column = "address"),
                @FieldResult(name = "user.username", column = "username")
        })
)
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address")
    private String address;

    @JsonProperty("user")
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Training> trainings = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private Set<Trainer> trainers;

    /**
     * Retrieves the username of the current user.
     *
     * <p>This method returns the username from the underlying {@link User} object.
     * It is used to fetch the unique identifier of the user for authentication and
     * authorization processes.</p>
     *
     * @return the username of the current user
     */
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Trainee trainee = (Trainee) obj;
        return Objects.equals(id, trainee.id)
                && Objects.equals(dateOfBirth, trainee.dateOfBirth)
                && Objects.equals(address, trainee.address)
                && Objects.equals(user, trainee.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateOfBirth, address, user);
    }
}
