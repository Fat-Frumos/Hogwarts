package com.epam.esm.gym.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entity representing an authentication token.
 *
 * <p>This class defines the structure of a token used for authentication, including its type, value, and validity.
 * It includes fields for the token type, access token, expiration details, and association with a {@link User}.</p>
 */
@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
public class Token implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Builder.Default
    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;
    @Column(name = "access_token", nullable = false, unique = true)
    private String accessToken;
    @Column(name = "access_token_ttl")
    private Long accessTokenTTL;
    private boolean revoked;
    private boolean expired;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Token token = (Token) obj;
        return revoked == token.revoked && expired == token.expired
                && Objects.equals(id, token.id) && tokenType == token.tokenType
                && Objects.equals(accessToken, token.accessToken)
                && Objects.equals(accessTokenTTL, token.accessTokenTTL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tokenType, accessToken, accessTokenTTL, revoked, expired);
    }

    /**
     * Enumeration representing the type of authentication token.
     *
     * <p>This enum defines the various types of tokens that can be used in the authentication process.</p>
     */
    public enum TokenType {

        /**
         * Represents a Bearer token type, commonly used in HTTP authentication headers.
         */
        BEARER
    }

}
