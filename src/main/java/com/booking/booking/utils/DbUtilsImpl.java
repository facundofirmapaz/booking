package com.booking.booking.utils;

import com.booking.booking.dtos.FlightDto;
import com.booking.booking.dtos.HotelDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.ArrayList;

@Repository
public class DbUtilsImpl implements DbUtils
{
    @Override
    public ArrayList<HotelDto> loadHotelsDataBase(String path)
    {
        File file;
        ArrayList<HotelDto> hoteles = null;

        try
        {
            file = ResourceUtils.getFile(path);
            ObjectMapper objectMapper = new ObjectMapper();
            com.fasterxml.jackson.core.type.TypeReference<ArrayList<HotelDto>> typeReference = new com.fasterxml.jackson.core.type.TypeReference<>() {};

            hoteles = objectMapper.readValue(file, typeReference);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return hoteles;
    }

    @Override
    public ArrayList<FlightDto> loadFlightsDataBase(String path) {
        File file;
        ArrayList<FlightDto> flightDtos = null;

        try
        {
            file = ResourceUtils.getFile(path);
            ObjectMapper objectMapper = new ObjectMapper();
            com.fasterxml.jackson.core.type.TypeReference<ArrayList<FlightDto>> typeReference = new com.fasterxml.jackson.core.type.TypeReference<>() {};

            flightDtos = objectMapper.readValue(file, typeReference);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return flightDtos;
    }
}
