package com.booking.booking.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    @DisplayName("Date string with valid format")
    void validFormat() { assertTrue(DateUtils.validFormat("11/09/2021")); }

    @Test
    @DisplayName("Date string with invalid format")
    void notValidFormat()
    {
        assertFalse(DateUtils.validFormat("1/09/2021"));
    }

    @Test
    @DisplayName("Valid range with valids formats dates and the range is valid")
    void validRangeWithValidsFormats() { assertTrue(DateUtils.validRange("05/05/2021", "06/05/2021")); }

    @Test
    @DisplayName("Invalid range with valids formats dates and the range is valid")
    void invalidRangeWithValidsFormats() { assertFalse(DateUtils.validRange("07/05/2021", "06/05/2021")); }

    @Test
    @DisplayName("To LocalDate with valid format and equals date")
    void toLocalDateWithValidFormatEquals()
    {
        LocalDate expected = LocalDate.of(2021, 5, 5);

        LocalDate actual = DateUtils.toLocalDate("05/05/2021");

        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("To LocalDate with valid format and not equals date")
    void toLocalDateWithValidFormatNotEquals()
    {
        LocalDate expected = LocalDate.of(2021, 05, 05);

        LocalDate actual = DateUtils.toLocalDate("05/05/2020");

        assertNotEquals(expected, actual);

    }

    @Test
    @DisplayName("To LocalDate with invalid format")
    void toLocalDateWithInvalidFormat() { assertNull(DateUtils.toLocalDate("34/01/2021")); }
}