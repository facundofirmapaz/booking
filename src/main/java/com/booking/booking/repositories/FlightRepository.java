package com.booking.booking.repositories;

import com.booking.booking.dtos.FlightDto;

import java.time.LocalDate;
import java.util.ArrayList;

public interface FlightRepository 
{
    ArrayList<FlightDto> getAll();

    boolean validateOrigin(String value);

    boolean validateDestination(String value);

    ArrayList<FlightDto> getByRangeOriginAndDestination(LocalDate dateFrom, LocalDate dateTo, String origin, String destination);

    boolean flightAvailableInRange(LocalDate dateFrom, LocalDate dateTo, FlightDto flightDto);
}
