package com.epam.esm.gym.dto.trainee;

import com.epam.esm.gym.dto.auth.BaseResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;

/**
 * Represents a slim version of a trainee profile, including essential personal information
 * and contact details. This class is used in scenarios where a full profile is not required.
 * The class is annotated with Lombok annotations to automatically generate getters,
 * a toString() method, a builder, a no-args constructor, and an all-args constructor.
 */
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SlimTraineeProfile implements BaseResponse {
    protected String firstName;
    protected String lastName;
    protected String username;
    protected String address;
    protected Boolean active;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate dateOfBirth;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        SlimTraineeProfile that = (SlimTraineeProfile) obj;
        return new EqualsBuilder()
                .append(firstName, that.firstName)
                .append(lastName, that.lastName)
                .append(username, that.username)
                .append(address, that.address)
                .append(active, that.active)
                .append(dateOfBirth, that.dateOfBirth)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(firstName).append(lastName)
                .append(username).append(address)
                .append(active).append(dateOfBirth)
                .toHashCode();
    }
}
