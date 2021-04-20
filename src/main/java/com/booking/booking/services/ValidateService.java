package com.booking.booking.services;

import com.booking.booking.dtos.BookingDto;
import com.booking.booking.dtos.HotelDto;
import com.booking.booking.dtos.NewReservDto;
import com.booking.booking.dtos.PaymentMethodDto;

public interface ValidateService
{
    String validateFormat(String value);

    boolean validRange(String dateFrom, String dateTo);

    boolean validPeopleQuantity(BookingDto booking);

    boolean validTypeOfRoom(BookingDto booking, HotelDto hotelDto);

    double calculateInterest(PaymentMethodDto paymentMethod);

    boolean validateEmailFormat(String userName);

    double calculateSubtotal(NewReservDto newReservDto, double price);
}
