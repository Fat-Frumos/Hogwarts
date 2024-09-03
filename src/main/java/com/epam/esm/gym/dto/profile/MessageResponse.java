package com.epam.esm.gym.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Represents a response containing a message.
 *
 * <p>This class is used to encapsulate a single message to be returned in API responses,
 * typically for confirmation or error reporting.</p>
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    /**
     * The message content.
     *
     * <p>This field holds the text of the message that will be returned in the response.</p>
     */
    private String message;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MessageResponse response = (MessageResponse) obj;
        return Objects.equals(message, response.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
