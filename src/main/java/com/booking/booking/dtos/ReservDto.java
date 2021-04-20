package com.booking.booking.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservDto
{
    private String userName;
    private double amount;
    private double interest;
    private double total;
    private BookingDto booking;
    private StatusCodeDto statusCode;
}
