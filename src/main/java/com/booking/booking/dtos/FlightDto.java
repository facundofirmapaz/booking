package com.booking.booking.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class FlightDto
{
    private String flightNumber;
    private String origin;
    private String destination;
    private String seatType;
    private double price;
    private String dateFrom;
    private String dateTo;
}