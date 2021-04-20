package com.booking.booking.controllers;

import com.booking.booking.dtos.ErrorDto;
import com.booking.booking.exceptionsHandlers.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController
{
    @ExceptionHandler(BadParameterException.class)
    public ResponseEntity<?> handleException(BadParameterException e)
    {
        ErrorDto errorDto = new ErrorDto("Error en los parametros enviados", e.getMessage());

        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(DateFormatNotValidException.class)
    public ResponseEntity<?> handleException(DateFormatNotValidException e)
    {
        ErrorDto errorDto = new ErrorDto("Error en el formato de fecha", e.getMessage());

        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(DestinationNotValidException.class)
    public ResponseEntity<?> handleException(DestinationNotValidException e)
    {
        ErrorDto errorDto = new ErrorDto("Error en el destino.", e.getMessage());

        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(InvalidRangeException.class)
    public ResponseEntity<?> handleException(InvalidRangeException e)
    {
        ErrorDto errorDto = new ErrorDto("Error en el rango de fechas.", e.getMessage());

        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(EmailFormatException.class)
    public ResponseEntity<?> handleException(EmailFormatException e)
    {
        ErrorDto errorDto = new ErrorDto("Error en el formato de mail.", e.getMessage());

        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<?> handleException(HotelNotFoundException e)
    {
        ErrorDto errorDto = new ErrorDto("Hotel no encontrado.", e.getMessage());

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPaymentMethodException.class)
    public ResponseEntity<?> handleException(InvalidPaymentMethodException e)
    {
        ErrorDto errorDto = new ErrorDto("Error en el medio de pago utilizado.", e.getMessage());

        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(InvalidPeopleQuantityException.class)
    public ResponseEntity<?> handleException(InvalidPeopleQuantityException e)
    {
        ErrorDto errorDto = new ErrorDto("Error en la cantidad de personas.", e.getMessage());

        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(InvalidRoomException.class)
    public ResponseEntity<?> handleException(InvalidRoomException e)
    {
        ErrorDto errorDto = new ErrorDto("Error en la habitacion seleccionada.", e.getMessage());

        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(RoomReservedException.class)
    public ResponseEntity<?> handleException(RoomReservedException e)
    {
        ErrorDto errorDto = new ErrorDto("Error en la habitacion seleccionada.", e.getMessage());

        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(OriginNotValidException.class)
    public ResponseEntity<?> handleException(OriginNotValidException e)
    {
        ErrorDto errorDto = new ErrorDto("Error en el origen seleccionado.", e.getMessage());

        return ResponseEntity.badRequest().body(errorDto);
    }
}
