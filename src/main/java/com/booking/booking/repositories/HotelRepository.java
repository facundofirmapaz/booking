package com.booking.booking.repositories;

import com.booking.booking.dtos.HotelDto;
import com.booking.booking.dtos.ReservDto;

import java.time.LocalDate;
import java.util.ArrayList;

public interface HotelRepository
{
    ArrayList<HotelDto> getAll();

    boolean validateDestination(String value);

    boolean hotelIsFreeInRange(LocalDate dateFrom, LocalDate dateTo, HotelDto hotel);

    ArrayList<HotelDto> getByRangeAndDestination(LocalDate dateFrom, LocalDate dateTo, String destination);

    HotelDto getById(String hotelCode);

    void addReserva(ReservDto resp);
}
