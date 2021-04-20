package com.booking.booking.services;

import com.booking.booking.dtos.*;
import com.booking.booking.exceptionsHandlers.BadParameterException;
import com.booking.booking.exceptionsHandlers.DestinationNotValidException;
import com.booking.booking.exceptionsHandlers.EmailFormatException;
import com.booking.booking.exceptionsHandlers.RoomReservedException;
import com.booking.booking.repositories.HotelRepository;
import com.booking.booking.utils.DateUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Map;

@Service
public class HotelServiceImpl implements HotelService
{
    private final ValidateService validateService;
    private final HotelRepository hotelRepository;

    public HotelServiceImpl(HotelRepository hotelRepository, ValidateService validateService)
    {
        this.hotelRepository = hotelRepository;
        this.validateService = validateService;
    }

    @SneakyThrows
    @Override
    public ArrayList<HotelDto> get(Map<String, String> params) {
        ArrayList<HotelDto> hotels = new ArrayList<>();
        String dateFrom  = "";
        String dateTo = "";
        String destination = "";

        if (params.isEmpty())
            hotels = hotelRepository.getAll();
        else {
            for (Map.Entry<String, String> filtro : params.entrySet())
                switch (filtro.getKey()) {
                    case "dateFrom": dateFrom = validateService.validateFormat(filtro.getValue());
                        break;
                    case "dateTo": dateTo = validateService.validateFormat(filtro.getValue());
                        break;
                    case "destination":
                        if(!hotelRepository.validateDestination(filtro.getValue()))
                            throw new DestinationNotValidException("El destino elegido no existe: " + filtro.getValue());
                        destination = filtro.getValue();
                        break;
                    default: throw new BadParameterException("Parametro no valido: " + filtro.getKey());
                }
            if (validateService.validRange(dateFrom, dateTo))
                hotels = hotelRepository.getByRangeAndDestination(DateUtils.toLocalDate(dateFrom), DateUtils.toLocalDate(dateTo), destination);
        }
        return hotels;
    }

    @SneakyThrows
    @Override
    public ReservDto newReserv(NewReservDto newReservDto) {
        ReservDto resp;
        var dateFrom = validateService.validateFormat(newReservDto.getBooking().getDateFrom());
        var dateTo = validateService.validateFormat(newReservDto.getBooking().getDateTo());
        var validRange = validateService.validRange(dateFrom, dateTo);
        HotelDto hotelDto = hotelRepository.getById(newReservDto.getBooking().getHotelCode());

        if(!hotelDto.getCity().equals(newReservDto.getBooking().getDestination()))
            throw new DestinationNotValidException("El destino elegido no existe: " + newReservDto.getBooking().getDestination());

        boolean validPeopleCant = validateService.validPeopleQuantity(newReservDto.getBooking());
        boolean validRoom = validateService.validTypeOfRoom(newReservDto.getBooking(), hotelDto);
        double interest = validateService.calculateInterest(newReservDto.getBooking().getPaymentMethod());

        if(!validateService.validateEmailFormat(newReservDto.getUserName()))
            throw new EmailFormatException("Formato de mail no valido");

        if (validRange && validPeopleCant && validRoom) {
            if(!hotelRepository.hotelIsFreeInRange(DateUtils.toLocalDate(dateFrom), DateUtils.toLocalDate(dateTo), hotelDto))
                throw new RoomReservedException("La habitacion ya se encuentra reservada para este rango de fechas");

            double subTotal = validateService.calculateSubtotal(newReservDto, hotelDto.getPrice());
            var status = new StatusCodeDto(200, "Reserva creada con exito");
            double total = Math.round(subTotal * (1 + interest/100));
            resp = new ReservDto(newReservDto.getUserName(), subTotal, interest, total, newReservDto.getBooking(), status);
            hotelRepository.addReserva(resp);
        }
        else {
            var status = new StatusCodeDto(200, "No se pudo crear la reserva");
            resp = new ReservDto(newReservDto.getUserName(), 0, 0, 0, newReservDto.getBooking(), status);
        }
        return resp;
    }
}