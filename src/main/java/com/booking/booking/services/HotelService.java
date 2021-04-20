package com.booking.booking.services;

import com.booking.booking.dtos.HotelDto;
import com.booking.booking.dtos.NewReservDto;
import com.booking.booking.dtos.ReservDto;

import java.util.ArrayList;
import java.util.Map;

public interface HotelService
{
    ArrayList<HotelDto> get(Map<String, String> params);

    ReservDto newReserv(NewReservDto newReservDto);
}
