package com.booking.booking.services;

import com.booking.booking.dtos.FlightDto;

import java.util.ArrayList;
import java.util.Map;

public interface FlightService
{
    ArrayList<FlightDto> get(Map<String, String> params);
}
