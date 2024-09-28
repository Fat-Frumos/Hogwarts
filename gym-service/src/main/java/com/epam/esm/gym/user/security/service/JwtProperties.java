package com.epam.esm.gym.user.security.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Configuration properties for JWT (JSON Web Token) settings.
 * <p>
 * This class encapsulates the configuration properties related to JWT, including
 * the issuer, secret key, and token expiration durations for access and refresh tokens.
 * It is typically used to manage and access JWT configurations from application properties.
 * </p>
 */
@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {

    private Long expiration;
    /**
     * The issuer of the JWT, typically used to identify the origin of the token.
     * This value is used when generating the token to set the "iss" claim.
     */
    private String issuer;

    /**
     * The secret key used for signing and verifying the JWT.
     * This key must be kept secure and private to prevent unauthorized access to the tokens.
     */
    private String secret;

    /**
     * The expiration duration for the access token, in milliseconds.
     * This value defines how long the access token remains valid after issuance.
     */
    private long access;

    /**
     * The expiration duration for the refresh token, in milliseconds.
     * This value defines the lifespan of the refresh token, which is used to obtain a new access token.
     */
    private long refresh;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        JwtProperties that = (JwtProperties) obj;
        return access == that.access
                && refresh == that.refresh
                && Objects.equals(issuer, that.issuer)
                && Objects.equals(secret, that.secret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuer, secret, access, refresh);
    }
}
