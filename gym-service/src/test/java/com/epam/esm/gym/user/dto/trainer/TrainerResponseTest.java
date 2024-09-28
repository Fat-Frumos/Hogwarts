package com.epam.esm.gym.user.dto.trainer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test class for TrainerResponseTest and TrainerResponseDto functionalities.
 */
class TrainerResponseTest {

    @Test
    void testEquals() {
        TrainerResponse trainer1 = TrainerResponse.builder()
                .username("Pomona.Sprout")
                .firstName("Pomona")
                .lastName("Sprout")
                .build();

        TrainerResponse trainer2 = TrainerResponse.builder()
                .username("Pomona.Sprout")
                .firstName("Pomona")
                .lastName("Sprout")
                .build();

        assertEquals(trainer1, trainer2);
    }

    @Test
    void testHashCode() {
        TrainerResponse trainer1 = TrainerResponse.builder()
                .username("Severus.Snape")
                .firstName("Severus")
                .lastName("Snape")
                .build();

        TrainerResponse trainer2 = TrainerResponse.builder()
                .username("Severus.Snape")
                .firstName("Severus")
                .lastName("Snape")
                .build();

        assertEquals(trainer1.hashCode(), trainer2.hashCode());
    }

    @Test
    void testConstructor() {
        TrainerResponse trainer = new TrainerResponse("Pomona.Sprout", "Pomona", "Sprout");
        assertEquals("Pomona.Sprout", trainer.getUsername());
        assertEquals("Pomona", trainer.getFirstName());
        assertEquals("Sprout", trainer.getLastName());
    }

    @ParameterizedTest
    @MethodSource("provideDtoForEqualityTests")
    void testEquals(TrainerResponseDto dto1, TrainerResponseDto dto2, boolean expected) {
        assertEquals(expected, dto1.equals(dto2));
    }

    @ParameterizedTest
    @MethodSource("provideDtoForEqualityTests")
    void testHashCode(TrainerResponseDto dto1, TrainerResponseDto dto2, boolean expected) {
        if (expected) {
            assertEquals(dto1.hashCode(), dto2.hashCode());
        } else {
            assertNotEquals(dto1.hashCode(), dto2.hashCode());
        }
    }

    @Test
    void testBuilder() {
        TrainerResponseDto dto = TrainerResponseDto.builder()
                .username("Albus.Dumbledore")
                .firstName("Albus")
                .lastName("Dumbledore")
                .specialization("Transfiguration")
                .build();

        assertEquals("Albus.Dumbledore", dto.getUsername());
        assertEquals("Albus", dto.getFirstName());
        assertEquals("Dumbledore", dto.getLastName());
        assertEquals("Transfiguration", dto.getSpecialization());
    }

    @Test
    void testDefaultConstructor() {
        TrainerResponseDto dto = new TrainerResponseDto();
        assertNull(dto.getUsername());
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
        assertNull(dto.getSpecialization());
    }

    @Test
    void testParameterizedConstructor() {
        TrainerResponseDto dto = new TrainerResponseDto(
                "Luna.Lovegood", "Luna", "Lovegood", "Care of Magical Creatures");
        assertEquals("Luna.Lovegood", dto.getUsername());
        assertEquals("Luna", dto.getFirstName());
        assertEquals("Lovegood", dto.getLastName());
        assertEquals("Care of Magical Creatures", dto.getSpecialization());
    }

    static Stream<Arguments> provideDtoForEqualityTests() {
        TrainerResponseDto dto1 = TrainerResponseDto.builder()
                .username("Harry.Potter")
                .firstName("Harry")
                .lastName("Potter")
                .specialization("Defense Against the Dark Arts")
                .build();

        TrainerResponseDto dto2 = TrainerResponseDto.builder()
                .username("Harry.Potter")
                .firstName("Harry")
                .lastName("Potter")
                .specialization("Defense Against the Dark Arts")
                .build();

        TrainerResponseDto dto3 = TrainerResponseDto.builder()
                .username("Hermione.Granger")
                .firstName("Hermione")
                .lastName("Granger")
                .specialization("Potions")
                .build();

        return Stream.of(
                Arguments.of(dto1, dto2, true),
                Arguments.of(dto1, dto3, false)
        );
    }
}