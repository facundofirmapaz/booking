package com.booking.booking.services;

import com.booking.booking.dtos.BookingDto;
import com.booking.booking.dtos.HotelDto;
import com.booking.booking.dtos.NewReservDto;
import com.booking.booking.dtos.PaymentMethodDto;
import com.booking.booking.exceptionsHandlers.*;
import com.booking.booking.utils.DateUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidateServiceImpl implements ValidateService
{
    @SneakyThrows
    @Override
    public String validateFormat(String date)
    {
        if(!DateUtils.validFormat(date))
            throw new DateFormatNotValidException("Formato de fecha debe ser dd/mm/aaaa: " + date);
        return date;
    }

    @SneakyThrows
    @Override
    public boolean validRange(String dateFrom, String dateTo)
    {
        if (!DateUtils.validRange(dateFrom, dateTo))
            throw new InvalidRangeException("La fecha de entrada debe ser menor a la" +
                    "de salida y la fecha de salida debe ser mayor a la de entrada: " + dateFrom + " - " + dateTo);
        return true;
    }

    @SneakyThrows
    @Override
    public boolean validPeopleQuantity(BookingDto booking) {
        if(!(booking.getPeople().size() == booking.getPeopleAmount()))
            throw new InvalidPeopleQuantityException("Las cantidades de personas no coinciden entre si. People " +
                    booking.getPeople().size() + " " + "Cantidad " + booking.getPeopleAmount());

        if (booking.getPeopleAmount() <= 0 || booking.getPeopleAmount() > 10)
            throw new InvalidPeopleQuantityException("Cantidad invalida de personas");

        return true;
    }

    @SneakyThrows
    @Override
    public boolean validTypeOfRoom(BookingDto booking, HotelDto hotelDto) {
        if(!booking.getRoomType().equals(hotelDto.getType()))
            throw new InvalidRoomException("Los tipos de habitaciones no coinciden");
        boolean notValid = false;
        switch (booking.getRoomType())
        {
            case "Single": notValid = booking.getPeopleAmount() > 1;
                break;
            case "Doble": notValid = booking.getPeopleAmount() > 2;
                break;
            case "Triple": notValid = booking.getPeopleAmount() > 3;
                break;
            case "Multiple": notValid = booking.getPeopleAmount() > 10;
                break;
        }
        if(notValid)
            throw new InvalidRoomException("La cantidad de personas no esta admitida para esa habitacion");
        return true;
    }

    @SneakyThrows
    @Override
    public double calculateInterest(PaymentMethodDto paymentMethod) {
        double interestPercent = 0;
        ArrayList<Integer> hastaTresCuotas = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
        ArrayList<Integer> hastaSeisCuotas = new ArrayList<Integer>(Arrays.asList(4, 5, 6));
        switch (paymentMethod.getType())
        {
            case "DEBIT":
                if(paymentMethod.getDues() != 1)
                    throw new InvalidPaymentMethodException("La cantidad de cuotas permitida para tarjeta de debito es 1");
                break;
            case "CREDIT":
                if (hastaTresCuotas.contains(paymentMethod.getDues()))
                    interestPercent = 5;
                else if (hastaSeisCuotas.contains(paymentMethod.getDues()))
                    interestPercent = 10;
                else
                    throw new InvalidPaymentMethodException("Cantidad de cuotas no permitida, solo de 1 a 6");
                break;
            default: throw new InvalidPaymentMethodException("Medio de pago no permitido");
        }
        return interestPercent;
    }

    @Override
    public boolean validateEmailFormat(String userName) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userName);
        return  matcher.matches();
    }

    @Override
    public double calculateSubtotal(NewReservDto newReservDto, double price) {
        LocalDate dateFrom = DateUtils.toLocalDate(newReservDto.getBooking().getDateFrom());
        LocalDate dateTo = DateUtils.toLocalDate(newReservDto.getBooking().getDateTo());
        long days = ChronoUnit.DAYS.between(dateFrom, dateTo);

        return days * price * newReservDto.getBooking().getPeopleAmount();
    }
}
