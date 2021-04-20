package com.booking.booking.services;

import com.booking.booking.dtos.BookingDto;
import com.booking.booking.dtos.HotelDto;
import com.booking.booking.dtos.NewReservDto;
import com.booking.booking.dtos.PaymentMethodDto;
import com.booking.booking.exceptionsHandlers.*;
import com.booking.booking.utils.DateUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ValidateServiceImplTest
{
    ValidateServiceImpl validateService = new ValidateServiceImpl();

    @Test
    @DisplayName("For a invalid range of dates throws an InvalidRangeException")
    void shouldThrowAnInvalidRangeException()
    {
        assertThrows(InvalidRangeException.class, () -> validateService.validRange("12/02/2021", "12/01/2021"));
    }

    @Test
    @DisplayName("For a invalid format of date throws an DateFormatNotValidException")
    void shouldThrowAnDateFormatNotValidException()
    {
        assertThrows(DateFormatNotValidException.class, () -> validateService.validateFormat("1-/02/2021"));
    }

    @Test
    @DisplayName("Valid range of dates returns true")
    void shouldReturnsTrue()
    {
        assertTrue(validateService.validRange("12/02/2021", "12/03/2021"));
    }

    @Test
    @DisplayName("Valid format of date returns the given date")
    void shouldReturnTheGivenDate()
    {
        assertEquals("12/05/2021", validateService.validateFormat("12/05/2021"));
    }

    @SneakyThrows
    @Test
    @DisplayName("Valid mount of people for a room")
    void shouldReturnTrue()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String VALID_REQ = "src/test/resources/validBookingRequest.json";
        NewReservDto newReservDto = objectMapper.readValue(new File(VALID_REQ), new TypeReference<>(){});

        assertTrue(validateService.validPeopleQuantity(newReservDto.getBooking()));

    }

    @SneakyThrows
    @Test
    @DisplayName("Invalid mount of people for a room")
    void shouldThrowsAnInvalidPeopleQuantityException()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String VALID_REQ = "src/test/resources/validBookingRequest.json";
        NewReservDto newReservDto = objectMapper.readValue(new File(VALID_REQ), new TypeReference<>(){});

        newReservDto.getBooking().setPeopleAmount(4);

        assertThrows(InvalidPeopleQuantityException.class, () -> validateService.validPeopleQuantity(newReservDto.getBooking()));
    }

    @SneakyThrows
    @Test
    @DisplayName("Valid type of room")
    void returnsIfaRoomTypeIsValid()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String VALID_REQ = "src/test/resources/validBookingRequest.json";
        NewReservDto newReservDto = objectMapper.readValue(new File(VALID_REQ), new TypeReference<>(){});
        HotelDto hotel = new HotelDto();
        hotel.setType("Doble");

        assertTrue(validateService.validTypeOfRoom(newReservDto.getBooking(), hotel));
    }

    @SneakyThrows
    @Test
    @DisplayName("Type of room not the same of booking")
    void throwsAnInvalidRoomException()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String VALID_REQ = "src/test/resources/validBookingRequest.json";
        NewReservDto newReservDto = objectMapper.readValue(new File(VALID_REQ), new TypeReference<>(){});
        HotelDto hotel = new HotelDto();
        hotel.setType("Triple");

        assertThrows(InvalidRoomException.class,
                () -> validateService.validTypeOfRoom(newReservDto.getBooking(), hotel));
    }

    @SneakyThrows
    @Test
    @DisplayName("Valid type of room but an invalid mount of people")
    void shouldThrowsAnInvalidRoomException()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String VALID_REQ = "src/test/resources/validBookingRequest.json";
        NewReservDto newReservDto = objectMapper.readValue(new File(VALID_REQ), new TypeReference<>(){});
        newReservDto.getBooking().setPeopleAmount(3);
        HotelDto hotel = new HotelDto();
        hotel.setType("Doble");

        assertThrows(InvalidRoomException.class,
                () -> validateService.validTypeOfRoom(newReservDto.getBooking(), hotel));

    }

    @Test
    @DisplayName("Invalid payment method")
    void shouldThrowsAnInvalidPaymentException()
    {
        PaymentMethodDto paymentMethodDto = new PaymentMethodDto();
        paymentMethodDto.setType("DEBIT");
        paymentMethodDto.setDues(2);
        assertThrows(InvalidPaymentMethodException.class,
                () -> validateService.calculateInterest(paymentMethodDto));
    }

    @Test
    @DisplayName("Valid payment method")
    void shouldReturnValidInterest()
    {
        PaymentMethodDto paymentMethodDto = new PaymentMethodDto();
        paymentMethodDto.setType("CREDIT");
        paymentMethodDto.setDues(2);
        double expectedInterest = 5;
        assertEquals(expectedInterest, validateService.calculateInterest(paymentMethodDto));
    }

    @Test
    @DisplayName("Valid format ofmail returns true")
    void validMailFormatReturnsTrue()
    {
        assertTrue(validateService.validateEmailFormat("facu@mail.com"));
    }

    @Test
    @DisplayName("Invalid format ofmail returns false")
    void invalidMailFormatReturnsFalse()
    {
        assertFalse(validateService.validateEmailFormat(".@.mail.com.."));
    }

    @Test
    @DisplayName("Calculate correctly a final price of the booking")
    void validFinalPriceOfBooking()
    {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setDateFrom("10/02/2021");
        bookingDto.setDateTo("13/02/2021"); //3 nights
        bookingDto.setPeopleAmount(3);

        NewReservDto newReservDto = new NewReservDto();
        newReservDto.setBooking(bookingDto);
        double price = 100;

        assertEquals(900, validateService.calculateSubtotal(newReservDto, price));
    }

}