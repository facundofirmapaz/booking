package com.booking.booking.services;

import com.booking.booking.dtos.FlightDto;
import com.booking.booking.dtos.HotelDto;
import com.booking.booking.exceptionsHandlers.BadParameterException;
import com.booking.booking.exceptionsHandlers.DestinationNotValidException;
import com.booking.booking.exceptionsHandlers.OriginNotValidException;
import com.booking.booking.repositories.FlightRepositoryImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FlightServiceImplTest {

    FlightRepositoryImpl flightRepository = Mockito.mock(FlightRepositoryImpl.class);
    ValidateServiceImpl validateService = Mockito.mock(ValidateServiceImpl.class);

    FlightServiceImpl flightService = new FlightServiceImpl(flightRepository, validateService);

    @SneakyThrows
    @Test
    @DisplayName("Get all the flights")
    void getAllFlights()
    {
        Map<String, String> emptyFilters = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        String ALL_FLIGHTS = "src/test/resources/allFlights.json";
        ArrayList<FlightDto> flightsExpected = objectMapper.readValue(new File(ALL_FLIGHTS), new TypeReference<>(){});

        Mockito.when(flightRepository.getAll()).thenReturn(flightsExpected);

        assertIterableEquals(flightsExpected, flightService.get(emptyFilters));
    }

    @SneakyThrows
    @Test
    @DisplayName("Get all the flights filter by valid filters")
    void getAllFlightsByValidFilters()
    {
        Map<String, String> filters = new HashMap<>();
        filters.put("dateFrom", "16/02/2021");
        filters.put("dateTo", "23/02/2021");
        filters.put("origin", "Bogotá");
        filters.put("destination", "Buenos Aires");

        LocalDate from = LocalDate.of(2021, 2, 16);
        LocalDate to = LocalDate.of(2021, 2, 23);
        String origin = "Bogotá";
        String dest = "Buenos Aires";

        ObjectMapper objectMapper = new ObjectMapper();
        String FLIGHTS_BY_VALID_FILTER = "src/test/resources/byValidFiltersFlights.json";
        ArrayList<FlightDto> flightsExpected = objectMapper.readValue(new File(FLIGHTS_BY_VALID_FILTER), new TypeReference<>(){});

        Mockito.when(validateService.validateFormat(filters.get("dateFrom"))).thenReturn("16/02/2021");
        Mockito.when(validateService.validateFormat(filters.get("dateTo"))).thenReturn("23/02/2021");
        Mockito.when(flightRepository.validateOrigin(origin)).thenReturn(true);
        Mockito.when(flightRepository.validateDestination(dest)).thenReturn(true);
        Mockito.when(validateService.validRange(filters.get("dateFrom"), filters.get("dateTo"))).thenReturn(true);
        Mockito.when(flightRepository.getByRangeOriginAndDestination(from, to, origin, dest)).thenReturn(flightsExpected);

        var actual = flightService.get(filters);

        assertEquals(flightsExpected, actual);
    }

    @Test
    @DisplayName("For a not valid origin throws an OriginNotValidException")
    void shouldThrowAnOriginNotValidException()
    {
        Map<String, String> filters = new HashMap<>();
        filters.put("dateFrom", "01/02/2021");
        filters.put("dateTo", "12/01/2021");
        filters.put("origin", "Santa Fe");
        filters.put("destination", "Buenos Aires");

        Mockito.when(flightRepository.validateOrigin(filters.get("origin"))).thenReturn(false);

        assertThrows(OriginNotValidException.class, () -> flightService.get(filters));
    }

    @Test
    @DisplayName("For a not valid destination throws an DestinationNotValidException")
    void shouldThrowAnDestinationNotValidException()
    {
        Map<String, String> filters = new HashMap<>();
        filters.put("dateFrom", "01/02/2021");
        filters.put("dateTo", "12/01/2021");
        filters.put("origin", "Bogotá");
        filters.put("destination", "Buenos Airessss");

        Mockito.when(flightRepository.validateOrigin(filters.get("origin"))).thenReturn(true);
        Mockito.when(flightRepository.validateDestination(filters.get("destination"))).thenReturn(false);

        assertThrows(DestinationNotValidException.class, () -> flightService.get(filters));
    }

    @Test
    @DisplayName("For a not valid parameter throws an BadParameterException")
    void shouldThrowAnBadParameterException()
    {
        Map<String, String> filters = new HashMap<>();
        filters.put("dateFrom", "01/02/2021");
        filters.put("dateTo", "12/01/2021");
        filters.put("origen", "Buenos Aires");
        filters.put("destination", "Buenos Aires");

        Mockito.when(validateService.validateFormat(filters.get("dateFrom"))).thenReturn("01/02/2021");
        Mockito.when(validateService.validateFormat(filters.get("dateTo"))).thenReturn("12/01/2021");
        Mockito.when(flightRepository.validateDestination(filters.get("destination"))).thenReturn(true);

        assertThrows(BadParameterException.class, () -> flightService.get(filters));
    }
}