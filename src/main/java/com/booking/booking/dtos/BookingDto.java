package com.booking.booking.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class BookingDto
{
    private String dateFrom;
    private String dateTo;
    private String destination;
    private String hotelCode;
    private int peopleAmount;
    private String roomType;
    private ArrayList<PersonDto> people;
    private PaymentMethodDto paymentMethod;
}