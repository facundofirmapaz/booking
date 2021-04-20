package com.booking.booking.exceptionsHandlers;

public class InvalidPaymentMethodException extends Exception
{
    public InvalidPaymentMethodException(String msj)
    {
        super(msj);
    }
}
