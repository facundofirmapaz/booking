package com.booking.booking.exceptionsHandlers;

public class InvalidRoomException extends Exception
{
    public InvalidRoomException(String msj)
    {
        super(msj);
    }
}
