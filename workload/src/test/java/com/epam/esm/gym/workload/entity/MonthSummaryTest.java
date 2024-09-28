package com.epam.esm.gym.workload.entity;

import org.junit.jupiter.api.Test;

import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the MonthSummaryTest class.
 */
class MonthSummaryTest {

    @Test
    void testDefaultConstructor() {
        MonthSummary monthSummary = new MonthSummary();
        assertThat(monthSummary).isNotNull();
        assertThat(monthSummary.getId()).isNull();
        assertThat(monthSummary.getMonth()).isNull();
        assertThat(monthSummary.getTotalDuration()).isEqualTo(0);
        assertThat(monthSummary.getYearSummary()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        YearSummary yearSummary = new YearSummary();
        MonthSummary monthSummary = new MonthSummary(1L, Month.JANUARY, 120L, yearSummary);

        assertThat(monthSummary.getId()).isEqualTo(1L);
        assertThat(monthSummary.getMonth()).isEqualTo(Month.JANUARY);
        assertThat(monthSummary.getTotalDuration()).isEqualTo(120L);
        assertThat(monthSummary.getYearSummary()).isEqualTo(yearSummary);
    }

    @Test
    void testEqualsAndHashCode() {
        YearSummary yearSummary1 = new YearSummary();
        MonthSummary monthSummary1 = new MonthSummary(1L, Month.JANUARY, 120L, yearSummary1);
        MonthSummary monthSummary2 = new MonthSummary(1L, Month.JANUARY, 120L, yearSummary1);
        assertThat(monthSummary1).isEqualTo(monthSummary2);
        assertThat(monthSummary1.hashCode()).isEqualTo(monthSummary2.hashCode());

        monthSummary2.setTotalDuration(130L);
        assertThat(monthSummary1).isNotEqualTo(monthSummary2);
    }

    @Test
    void testToString() {
        YearSummary yearSummary = new YearSummary();
        MonthSummary monthSummary = new MonthSummary(1L, Month.JANUARY, 120L, yearSummary);

        String expectedString = "MonthSummary(id=1, month=JANUARY, totalDuration=120)";
        assertThat(monthSummary.toString()).isEqualTo(expectedString);
    }

    @Test
    void testGettersAndSetters() {
        MonthSummary monthSummary = new MonthSummary();
        YearSummary yearSummary = new YearSummary();

        monthSummary.setId(1L);
        monthSummary.setMonth(Month.FEBRUARY);
        monthSummary.setTotalDuration(150L);
        monthSummary.setYearSummary(yearSummary);

        assertThat(monthSummary.getId()).isEqualTo(1L);
        assertThat(monthSummary.getMonth()).isEqualTo(Month.FEBRUARY);
        assertThat(monthSummary.getTotalDuration()).isEqualTo(150L);
        assertThat(monthSummary.getYearSummary()).isEqualTo(yearSummary);
    }
}
