package com.booking.booking.controllers;

import com.booking.booking.dtos.HotelDto;
import com.booking.booking.repositories.HotelRepositoryImpl;
import com.booking.booking.services.HotelServiceImpl;
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

class HotelControllerTest {

    HotelServiceImpl hotelService = Mockito.mock(HotelServiceImpl.class);

    HotelController hotelController = new HotelController(hotelService);

    @SneakyThrows
    @Test
    @DisplayName("Get all the hotels")
    void getAllHotels()
    {
        Map<String, String> emptyFilters = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        String ALL_HOTELS = "src/test/resources/allHotels.json";
        ArrayList<HotelDto> hotelsExpected = objectMapper.readValue(new File(ALL_HOTELS), new TypeReference<>(){});

        Mockito.when(hotelService.get(emptyFilters)).thenReturn(hotelsExpected);

        ResponseEntity<?> respuestaServicio = hotelController.get(emptyFilters);

        assertEquals(hotelsExpected, respuestaServicio.getBody());
    }
}