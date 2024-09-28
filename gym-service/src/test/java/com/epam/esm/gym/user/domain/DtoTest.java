package com.epam.esm.gym.user.domain;

import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.auth.RegisterRequest;
import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.security.service.JwtProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DtoTest {

    @Test
    void testEqualsAndHashCode() {
        JwtProperties jwtProperties1 = new JwtProperties();
        jwtProperties1.setIssuer("Issuer1");
        jwtProperties1.setSecret("Secret1");
        jwtProperties1.setAccess(3600L);
        jwtProperties1.setRefresh(7200L);

        JwtProperties jwtProperties2 = new JwtProperties();
        jwtProperties2.setIssuer("Issuer1");
        jwtProperties2.setSecret("Secret1");
        jwtProperties2.setAccess(3600L);
        jwtProperties2.setRefresh(7200L);

        JwtProperties jwtProperties3 = new JwtProperties();
        jwtProperties3.setIssuer("Issuer2");
        jwtProperties3.setSecret("Secret2");
        jwtProperties3.setAccess(7200L);
        jwtProperties3.setRefresh(14400L);

        assertEquals(jwtProperties1, jwtProperties2);
        assertNotEquals(jwtProperties1, jwtProperties3);
        assertEquals(jwtProperties1.hashCode(), jwtProperties2.hashCode());
        assertNotEquals(jwtProperties1.hashCode(), jwtProperties3.hashCode());
    }

    @Test
    void testGettersAndSetters() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setIssuer("Issuer1");
        jwtProperties.setSecret("Secret1");
        jwtProperties.setAccess(3600L);
        jwtProperties.setRefresh(7200L);

        assertEquals("Issuer1", jwtProperties.getIssuer());
        assertEquals("Secret1", jwtProperties.getSecret());
        assertEquals(3600L, jwtProperties.getAccess());
        assertEquals(7200L, jwtProperties.getRefresh());
    }

    @Test
    void testRegisterRequest() {
        RegisterRequest request1 = new RegisterRequest(
                "HarryPotter", "Harry", "Potter", "password123");
        RegisterRequest request2 = new RegisterRequest(
                "HarryPotter", "Harry", "Potter", "password123");
        RegisterRequest request3 = new RegisterRequest(
                "HermioneGranger", "Hermione", "Granger", "password456");

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
        assertEquals("HarryPotter", request1.getUsername());
        assertEquals("Harry", request1.getFirstName());
        assertEquals("Potter", request1.getLastName());
        assertEquals("password123", request1.getPassword());
    }

    @Test
    void testProfileResponse() {
        ProfileResponse login1 = new ProfileResponse("HarryPotter", "password123");
        ProfileResponse login2 = new ProfileResponse("HarryPotter", "password123");
        ProfileResponse login3 = new ProfileResponse("HermioneGranger", "password456");

        assertEquals(login1, login2);
        assertNotEquals(login1, login3);
        assertEquals(login1.hashCode(), login2.hashCode());
        assertNotEquals(login1.hashCode(), login3.hashCode());
        assertEquals("HarryPotter", login1.username());
        assertEquals("password123", login1.password());
    }

    @Test
    void testMessageResponse() {
        MessageResponse response1 = new MessageResponse("Success");
        MessageResponse response2 = new MessageResponse("Success");
        MessageResponse response3 = new MessageResponse("Error");

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
        assertEquals("Success", response1.message());
    }

    @Test
    void testLoginResponse() {
        ProfileResponse profile1 = new ProfileResponse("HarryPotter", "password123");
        ProfileResponse profile2 = new ProfileResponse("HarryPotter", "password123");
        ProfileResponse profile3 = new ProfileResponse("HermioneGranger", "password456");

        assertEquals(profile1, profile2);
        assertNotEquals(profile1, profile3);
        assertEquals(profile1.hashCode(), profile2.hashCode());
        assertNotEquals(profile1.hashCode(), profile3.hashCode());
        assertEquals("HarryPotter", profile1.username());
        assertEquals("password123", profile1.password());
    }
}
