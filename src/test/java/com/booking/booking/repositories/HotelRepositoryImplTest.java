package com.booking.booking.repositories;

import com.booking.booking.dtos.HotelDto;
import com.booking.booking.dtos.ReservDto;
import com.booking.booking.services.HotelServiceImpl;
import com.booking.booking.utils.DbUtils;
import com.booking.booking.utils.DbUtilsImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HotelRepositoryImplTest
{
    DbUtilsImpl dbUtils = Mockito.mock(DbUtilsImpl.class);

    HotelRepositoryImpl hotelRepository = new HotelRepositoryImpl(dbUtils);

    private final String path = "classpath:dbHotels.json";

    @SneakyThrows
    @Test
    @DisplayName("Get all the hotels")
    void getAll()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String ALL_HOTELS = "src/test/resources/allHotels.json";
        ArrayList<HotelDto> hotelsExpected = objectMapper.readValue(new File(ALL_HOTELS), new TypeReference<>(){});

        Mockito.when(dbUtils.loadHotelsDataBase(path)).thenReturn(hotelsExpected);

        var actual = hotelRepository.getAll();

        assertIterableEquals(hotelsExpected, actual);
    }

    @SneakyThrows
    @Test
    @DisplayName("Should return a valid destination")
    void validDestination()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String ALL_HOTELS = "src/test/resources/allHotels.json";
        ArrayList<HotelDto> hotelsExpected = objectMapper.readValue(new File(ALL_HOTELS), new TypeReference<>(){});

        Mockito.when(dbUtils.loadHotelsDataBase(path)).thenReturn(hotelsExpected);

        assertTrue(hotelRepository.validateDestination("Buenos Aires"));
    }

    @SneakyThrows
    @Test
    @DisplayName("Get by valid filters")
    void getByRangeAndDestination()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String ALL_HOTELS = "src/test/resources/byValidFiltersHotels.json";
        ArrayList<HotelDto> hotelsExpected = objectMapper.readValue(new File(ALL_HOTELS), new TypeReference<>(){});

        Mockito.when(dbUtils.loadHotelsDataBase(path)).thenReturn(hotelsExpected);

        LocalDate from = LocalDate.of(2021, 2, 12);
        LocalDate to = LocalDate.of(2021, 3, 12);
        String dest = "Buenos Aires";

        var actual = hotelRepository.getByRangeAndDestination(from, to, dest);

        assertIterableEquals(hotelsExpected, actual);

    }

    @SneakyThrows
    @Test
    @DisplayName("Validate if room is free in a invalid range of dates")
    void validateRoomWithInvalidRange()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String SIMPLE_HOTEL = "src/test/resources/simpleHotel.json";
        HotelDto hotel = objectMapper.readValue(new File(SIMPLE_HOTEL), new TypeReference<>(){});

        String RESERV = "src/test/resources/existingBooking.json";
        ReservDto reserv = objectMapper.readValue(new File(RESERV), new TypeReference<>(){});

        hotelRepository.addReserva(reserv);

        LocalDate from = LocalDate.of(2021, 2, 12);
        LocalDate to = LocalDate.of(2021, 3, 12);

        assertFalse(hotelRepository.hotelIsFreeInRange(from, to, hotel));

    }

    @SneakyThrows
    @Test
    @DisplayName("Should return a hotel by id")
    void getByValidId()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String ALL_HOTELS = "src/test/resources/allHotels.json";
        ArrayList<HotelDto> hotelDtos = objectMapper.readValue(new File(ALL_HOTELS), new TypeReference<>(){});

        String SIMPLE_HOTEL = "src/test/resources/simpleHotel.json";
        HotelDto hotel = objectMapper.readValue(new File(SIMPLE_HOTEL), new TypeReference<>(){});

        Mockito.when(dbUtils.loadHotelsDataBase(path)).thenReturn(hotelDtos);

        assertEquals(hotel, hotelRepository.getById("BH-0002"));
    }
}