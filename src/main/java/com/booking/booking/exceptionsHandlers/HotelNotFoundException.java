package com.booking.booking.exceptionsHandlers;

public class HotelNotFoundException extends Exception
{
    public HotelNotFoundException(String msj) {
        super(msj);
    }
}
