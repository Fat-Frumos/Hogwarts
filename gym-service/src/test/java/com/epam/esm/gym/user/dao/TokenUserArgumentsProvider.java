package com.epam.esm.gym.user.dao;

import com.epam.esm.gym.user.entity.RoleType;
import com.epam.esm.gym.user.entity.Token;
import com.epam.esm.gym.user.entity.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Provides arguments for parameterized tests involving {@link Token} and {@link User} entities.
 * This class generates a stream of arguments to be used in testing various scenarios
 * related to tokens and users.
 */
public class TokenUserArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        User harry = User.builder()
                .id(1)
                .firstName("Harry")
                .lastName("Potter")
                .username("Harry.Potter")
                .password("password123")
                .active(true)
                .permission(RoleType.ROLE_TRAINEE)
                .build();

        Set<Token> expectedTokens = new HashSet<>();
        expectedTokens.add(Token.builder()
                .id(1)
                .tokenType(Token.TokenType.BEARER)
                .accessToken("validJwtToken1")
                .accessTokenTTL(3600L)
                .revoked(false)
                .expired(false)
                .user(harry)
                .build());
        expectedTokens.add(Token.builder()
                .id(3)
                .tokenType(Token.TokenType.BEARER)
                .accessToken("invalidJwtToken")
                .accessTokenTTL(3600L)
                .revoked(true)
                .expired(true)
                .user(harry)
                .build());

        return Stream.of(
                Arguments.of(1, expectedTokens)
        );
    }
}
