package com.booking.booking.repositories;

import com.booking.booking.dtos.FlightDto;
import com.booking.booking.utils.DateUtils;
import com.booking.booking.utils.DbUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Repository
public class FlightRepositoryImpl implements FlightRepository
{
    private final DbUtils dbUtils;

    public FlightRepositoryImpl(DbUtils dbUtils) { this.dbUtils = dbUtils; }

    @Override
    public ArrayList<FlightDto> getAll() {
        ArrayList<FlightDto> flightDtos = new ArrayList<>();

        var data = dbUtils.loadFlightsDataBase("classpath:dbFlights.json");

        if (data != null)
            flightDtos = data;

        return flightDtos;
    }

    @Override
    public boolean validateOrigin(String origin) {
        ArrayList<FlightDto> flightDtos = new ArrayList<>();
        var data = dbUtils.loadFlightsDataBase("classpath:dbFlights.json");

        if (data != null)
            flightDtos = data;

        var hotel = flightDtos.stream().filter(f -> f.getOrigin().equals(origin)).findFirst();

        return hotel.isPresent();
    }

    @Override
    public boolean validateDestination(String destination) {
        ArrayList<FlightDto> flightDtos = new ArrayList<>();
        var data = dbUtils.loadFlightsDataBase("classpath:dbFlights.json");

        if (data != null)
            flightDtos = data;

        var hotel = flightDtos.stream().filter(f -> f.getDestination().equals(destination)).findFirst();

        return hotel.isPresent();
    }

    @Override
    public ArrayList<FlightDto> getByRangeOriginAndDestination(LocalDate dateFrom, LocalDate dateTo, String origin, String destination) {
        ArrayList<FlightDto> flightDtos = new ArrayList<>();
        var data = dbUtils.loadFlightsDataBase("classpath:dbFlights.json");

        if (data != null)
            flightDtos = data;

        var resp = flightDtos.stream()
                .filter(f -> f.getDestination().equals(destination) &&
                        f.getOrigin().equals(origin) &&
                        flightAvailableInRange(dateFrom, dateTo, f))
                .collect(Collectors.toList());

        return (ArrayList<FlightDto>) resp;
    }

    @Override
    public boolean flightAvailableInRange(LocalDate dateFrom, LocalDate dateTo, FlightDto flightDto) {
        LocalDate flightAvailableFrom =  DateUtils.toLocalDate(flightDto.getDateFrom());
        LocalDate flightAvailableTo = DateUtils.toLocalDate(flightDto.getDateTo());

        return flightAvailableFrom.compareTo(dateFrom) <= 0 &&
                dateTo.compareTo(flightAvailableTo) <= 0;
    }
}
