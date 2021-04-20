package com.booking.booking.utils;

import com.booking.booking.dtos.FlightDto;
import com.booking.booking.dtos.HotelDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DbUtilsImplTest {

    DbUtilsImpl dbUtils = new DbUtilsImpl();

    @SneakyThrows
    @Test
    @DisplayName("Valid path returns database of hotels")
    void loadHotelsDataBase()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String ALL_HOTELS = "src/test/resources/allHotels.json";
        ArrayList<HotelDto> hotelsExpected = objectMapper.readValue(new File(ALL_HOTELS), new TypeReference<>(){});

        var actual = dbUtils.loadHotelsDataBase(ALL_HOTELS);

        assertIterableEquals(hotelsExpected, actual);

    }

    @SneakyThrows
    @Test
    @DisplayName("Not Valid path returns null")
    void loadHotelsDataBaseShouldReturnNull()
    {
        String ALL_HOTELS = "src/test/resources/allHotels.jsonn";

        assertNull(dbUtils.loadHotelsDataBase(ALL_HOTELS));
    }

    @SneakyThrows
    @Test
    @DisplayName("Valid path returns database of flights")
    void loadflightsDataBase()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String ALL_FLIGHTS = "src/test/resources/allFlights.json";
        ArrayList<FlightDto> expected = objectMapper.readValue(new File(ALL_FLIGHTS), new TypeReference<>(){});

        var actual = dbUtils.loadFlightsDataBase(ALL_FLIGHTS);

        assertIterableEquals(expected, actual);

    }
}