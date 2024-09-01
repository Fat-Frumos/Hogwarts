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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return revoked == token.revoked && expired == token.expired && Objects.equals(id, token.id) && tokenType == token.tokenType && Objects.equals(accessToken, token.accessToken) && Objects.equals(accessTokenTTL, token.accessTokenTTL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tokenType, accessToken, accessTokenTTL, revoked, expired);
    }
}
