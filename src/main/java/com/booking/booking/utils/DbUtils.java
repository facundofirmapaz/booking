package com.booking.booking.utils;


import com.booking.booking.dtos.FlightDto;
import com.booking.booking.dtos.HotelDto;
import java.util.ArrayList;

public interface DbUtils
{
    ArrayList<HotelDto> loadHotelsDataBase(String path);

    ArrayList<FlightDto> loadFlightsDataBase(String s);
}
