package com.booking.booking.services;

import com.booking.booking.dtos.HotelDto;
import com.booking.booking.dtos.NewReservDto;
import com.booking.booking.dtos.ReservDto;
import com.booking.booking.exceptionsHandlers.BadParameterException;
import com.booking.booking.exceptionsHandlers.DateFormatNotValidException;
import com.booking.booking.exceptionsHandlers.DestinationNotValidException;
import com.booking.booking.exceptionsHandlers.InvalidRangeException;
import com.booking.booking.repositories.HotelRepositoryImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HotelServiceImplTest
{

    HotelRepositoryImpl hotelRepositoryImpl = Mockito.mock(HotelRepositoryImpl.class);
    ValidateServiceImpl validateService = Mockito.mock(ValidateServiceImpl.class);

    HotelServiceImpl hotelService = new HotelServiceImpl(hotelRepositoryImpl, validateService);

    @SneakyThrows
    @Test
    @DisplayName("Get all the hotels")
    void getAllHotels()
    {
        Map<String, String> emptyFilters = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        String ALL_HOTELS = "src/test/resources/allHotels.json";
        ArrayList<HotelDto> hotelsExpected = objectMapper.readValue(new File(ALL_HOTELS), new TypeReference<>(){});

        Mockito.when(hotelRepositoryImpl.getAll()).thenReturn(hotelsExpected);

        assertIterableEquals(hotelsExpected, hotelService.get(emptyFilters));
    }

    @SneakyThrows
    @Test
    @DisplayName("Get all the hotels filter by valid filters")
    void getAllHotelsByValidFilters()
    {
        Map<String, String> filters = new HashMap<>();
        filters.put("dateFrom", "12/02/2021");
        filters.put("dateTo", "12/03/2021");
        filters.put("destination", "Buenos Aires");

        LocalDate from = LocalDate.of(2021, 2, 12);
        LocalDate to = LocalDate.of(2021, 3, 12);
        String dest = "Buenos Aires";

        ObjectMapper objectMapper = new ObjectMapper();
        String HOTELS_BY_VALID_FILTER = "src/test/resources/byValidFiltersHotels.json";
        ArrayList<HotelDto> hotelsExpected = objectMapper.readValue(new File(HOTELS_BY_VALID_FILTER), new TypeReference<>(){});

        Mockito.when(validateService.validateFormat(filters.get("dateFrom"))).thenReturn("12/02/2021");
        Mockito.when(validateService.validateFormat(filters.get("dateTo"))).thenReturn("12/03/2021");
        Mockito.when(hotelRepositoryImpl.validateDestination(dest)).thenReturn(true);
        Mockito.when(validateService.validRange(filters.get("dateFrom"), filters.get("dateTo"))).thenReturn(true);
        Mockito.when(hotelRepositoryImpl.getByRangeAndDestination(from, to, dest)).thenReturn(hotelsExpected);

        var actual = hotelService.get(filters);

        assertEquals(hotelsExpected, actual);
    }

    @Test
    @DisplayName("For a not valid destination throws an DestinationNotValidException")
    void shouldThrowAnDestinationNotValidException()
    {
        Map<String, String> filters = new HashMap<>();
        filters.put("dateFrom", "01/02/2021");
        filters.put("dateTo", "12/01/2021");
        filters.put("destination", "Buenos Aire");

        Mockito.when(hotelRepositoryImpl.validateDestination(filters.get("destination"))).thenReturn(false);

        assertThrows(DestinationNotValidException.class, () -> hotelService.get(filters));
    }

    @Test
    @DisplayName("For a not valid parameter throws an BadParameterException")
    void shouldThrowAnBadParameterException()
    {
        Map<String, String> filters = new HashMap<>();
        filters.put("date", "01/02/2021");
        filters.put("dateTo", "12/01/2021");
        filters.put("destination", "Buenos Aires");

        Mockito.when(hotelRepositoryImpl.validateDestination(filters.get("destination"))).thenReturn(true);

        assertThrows(BadParameterException.class, () -> hotelService.get(filters));
    }

    @SneakyThrows
    @Test
    @DisplayName("A valid newReservDto adds a new reserv and returns the reservDto")
    void addAnewReserv()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String VALID_REQ = "src/test/resources/validBookingRequest.json";
        NewReservDto newReservDto = objectMapper.readValue(new File(VALID_REQ), new TypeReference<>(){});

        String SIMPLE_HOTEL = "src/test/resources/simpleHotel.json";
        HotelDto hotelDto = objectMapper.readValue(new File(SIMPLE_HOTEL), new TypeReference<>(){});

        String EXISTING_BOOKING = "src/test/resources/existingBooking.json";
        ReservDto expected = objectMapper.readValue(new File(EXISTING_BOOKING), new TypeReference<>(){});

        LocalDate from = LocalDate.of(2021, 2, 13);
        LocalDate to = LocalDate.of(2021, 2, 15);

        Mockito.when(validateService.validateFormat(newReservDto.getBooking().getDateFrom())).thenReturn("13/02/2021");
        Mockito.when(validateService.validateFormat(newReservDto.getBooking().getDateTo())).thenReturn("15/02/2021");
        Mockito.when(validateService.validRange(newReservDto.getBooking().getDateFrom(), newReservDto.getBooking().getDateTo())).thenReturn(true);
        Mockito.when(hotelRepositoryImpl.getById(newReservDto.getBooking().getHotelCode())).thenReturn(hotelDto);
        Mockito.when(validateService.validPeopleQuantity(newReservDto.getBooking())).thenReturn(true);
        Mockito.when(validateService.validTypeOfRoom(newReservDto.getBooking(), hotelDto)).thenReturn(true);
        Mockito.when(validateService.calculateInterest(newReservDto.getBooking().getPaymentMethod())).thenReturn(10.0);
        Mockito.when(validateService.validateEmailFormat(newReservDto.getUserName())).thenReturn(true);
        Mockito.when(hotelRepositoryImpl.hotelIsFreeInRange(from, to, hotelDto)).thenReturn(true);
        Mockito.when(validateService.calculateSubtotal(newReservDto, hotelDto.getPrice())).thenReturn(28800.0);

        var actual = hotelService.newReserv(newReservDto);

        assertEquals(expected, actual);
    }

    @SneakyThrows
    @Test
    @DisplayName("Add new reserv with a invalid destination")
    void throwAnDestinationNotValidException()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String VALID_REQ = "src/test/resources/validBookingRequest.json";
        NewReservDto newReservDto = objectMapper.readValue(new File(VALID_REQ), new TypeReference<>(){});

        //change de destination to a invalid one
        newReservDto.getBooking().setDestination("Santa Fe");

        String SIMPLE_HOTEL = "src/test/resources/simpleHotel.json";
        HotelDto hotelDto = objectMapper.readValue(new File(SIMPLE_HOTEL), new TypeReference<>(){});

        Mockito.when(validateService.validateFormat(newReservDto.getBooking().getDateFrom())).thenReturn("13/02/2021");
        Mockito.when(validateService.validateFormat(newReservDto.getBooking().getDateTo())).thenReturn("15/02/2021");
        Mockito.when(validateService.validRange(newReservDto.getBooking().getDateFrom(), newReservDto.getBooking().getDateTo())).thenReturn(true);
        Mockito.when(hotelRepositoryImpl.getById(newReservDto.getBooking().getHotelCode())).thenReturn(hotelDto);


        assertThrows(DestinationNotValidException.class, () -> hotelService.newReserv(newReservDto));
    }
}