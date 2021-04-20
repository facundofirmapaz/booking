package com.booking.booking.controllers;

import com.booking.booking.dtos.FlightDto;
import com.booking.booking.services.FlightServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FlightControllerTest {

    FlightServiceImpl flightService = Mockito.mock(FlightServiceImpl.class);

    FlightController flightController = new FlightController(flightService);

    @SneakyThrows
    @Test
    @DisplayName("Get all the flights")
    void getAllFlights()
    {
        Map<String, String> emptyFilters = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        String ALL_FLIGHTS = "src/test/resources/allFlights.json";
        ArrayList<FlightDto> flightDtosExpected = objectMapper.readValue(new File(ALL_FLIGHTS), new TypeReference<>(){});

        Mockito.when(flightService.get(emptyFilters)).thenReturn(flightDtosExpected);

        ResponseEntity<?> respuestaServicio = flightController.get(emptyFilters);

        assertEquals(flightDtosExpected, respuestaServicio.getBody());
    }
}