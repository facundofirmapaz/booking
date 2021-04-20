package com.booking.booking.repositories;

import com.booking.booking.dtos.FlightDto;
import com.booking.booking.utils.DbUtilsImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FlightRepositoryImplTest {

    DbUtilsImpl dbUtils = Mockito.mock(DbUtilsImpl.class);

    FlightRepositoryImpl flightRepository = new FlightRepositoryImpl(dbUtils);

    private final String path = "classpath:dbFlights.json";

    @SneakyThrows
    @Test
    @DisplayName("Get all the flights")
    void getAll()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String ALL_FLIGHTS = "src/test/resources/allFlights.json";
        ArrayList<FlightDto> flightDtos = objectMapper.readValue(new File(ALL_FLIGHTS), new TypeReference<>(){});

        Mockito.when(dbUtils.loadFlightsDataBase(path)).thenReturn(flightDtos);

        var actual = flightRepository.getAll();

        assertIterableEquals(flightDtos, actual);
    }

    @SneakyThrows
    @Test
    @DisplayName("Get by valid filters")
    void getByRangeOriginAndDestination()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String ALL_FLIGHTS = "src/test/resources/byValidFiltersFlights.json";
        ArrayList<FlightDto> flightDtos = objectMapper.readValue(new File(ALL_FLIGHTS), new TypeReference<>(){});

        Mockito.when(dbUtils.loadFlightsDataBase(path)).thenReturn(flightDtos);

        LocalDate from = LocalDate.of(2021, 2, 16);
        LocalDate to = LocalDate.of(2021, 2, 23);
        String origin = "Bogotá";
        String dest = "Buenos Aires";

        var actual = flightRepository.getByRangeOriginAndDestination(from, to, origin, dest);

        assertIterableEquals(flightDtos, actual);

    }

    @SneakyThrows
    @Test
    @DisplayName("Should return a valid origin")
    void validateOrigin()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String ALL_FLIGHTS = "src/test/resources/allFlights.json";
        ArrayList<FlightDto> flightDtos = objectMapper.readValue(new File(ALL_FLIGHTS), new TypeReference<>(){});

        Mockito.when(dbUtils.loadFlightsDataBase(path)).thenReturn(flightDtos);

        assertTrue(flightRepository.validateOrigin("Bogotá"));
    }

    @SneakyThrows
    @Test
    @DisplayName("Should return a invalid destination")
    void validateAnInvalidDestination()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String ALL_FLIGHTS = "src/test/resources/allFlights.json";
        ArrayList<FlightDto> flightDtos = objectMapper.readValue(new File(ALL_FLIGHTS), new TypeReference<>(){});

        Mockito.when(dbUtils.loadFlightsDataBase(path)).thenReturn(flightDtos);

        assertFalse(flightRepository.validateDestination("Bogotaaaa"));
    }

    @SneakyThrows
    @Test
    @DisplayName("Checks available flights in the given range of dates")
    void flightAvailableInRange()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String SIMPLE_FLIGHT = "src/test/resources/simpleFlight.json";
        FlightDto flightDto = objectMapper.readValue(new File(SIMPLE_FLIGHT), new TypeReference<>(){});

        LocalDate from = LocalDate.of(2021, 2, 16);
        LocalDate to = LocalDate.of(2021, 2, 23);

        assertTrue(flightRepository.flightAvailableInRange(from, to, flightDto));
    }
}
