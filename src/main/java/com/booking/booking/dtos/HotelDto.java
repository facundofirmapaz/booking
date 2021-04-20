package com.booking.booking.dtos;

import com.booking.booking.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto
{
    private String hotelCode;
    private String name;
    private String city;
    private String type;
    private double price;
    private String dateFrom;
    private String dateTo;
    private boolean reserved;
}