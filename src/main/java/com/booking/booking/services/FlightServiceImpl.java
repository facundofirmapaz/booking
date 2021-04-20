package com.booking.booking.services;

import com.booking.booking.dtos.FlightDto;
import com.booking.booking.exceptionsHandlers.BadParameterException;
import com.booking.booking.exceptionsHandlers.DestinationNotValidException;
import com.booking.booking.exceptionsHandlers.OriginNotValidException;
import com.booking.booking.repositories.FlightRepository;
import com.booking.booking.utils.DateUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class FlightServiceImpl implements FlightService{

    private final FlightRepository flightRepository;
    private final ValidateService validateService;

    public FlightServiceImpl(FlightRepository flightRepository, ValidateService validateService)
    {
        this.flightRepository = flightRepository;
        this.validateService = validateService;
    }

    @SneakyThrows
    @Override
    public ArrayList<FlightDto> get(Map<String, String> params) {
        ArrayList<FlightDto> flightDtos = new ArrayList<>();
        String dateFrom  = "";
        String dateTo = "";
        String origin = "";
        String destination = "";

        if (params.isEmpty())
            flightDtos = flightRepository.getAll();
        else {
            for (Map.Entry<String, String> filtro : params.entrySet())
                switch (filtro.getKey()) {
                    case "dateFrom": dateFrom = validateService.validateFormat(filtro.getValue());
                        break;
                    case "dateTo": dateTo = validateService.validateFormat(filtro.getValue());
                        break;
                    case "origin":
                        if(!flightRepository.validateOrigin(filtro.getValue()))
                            throw new OriginNotValidException("El origen elegido no existe: " + filtro.getValue());
                        origin = filtro.getValue();
                        break;
                    case "destination":
                        if(!flightRepository.validateDestination(filtro.getValue()))
                            throw new DestinationNotValidException("El destino elegido no existe: " + filtro.getValue());
                        destination = filtro.getValue();
                        break;
                    default: throw new BadParameterException("Parametro no valido: " + filtro.getKey());
                }
            if (validateService.validRange(dateFrom, dateTo))
                flightDtos = flightRepository.getByRangeOriginAndDestination(DateUtils.toLocalDate(dateFrom), DateUtils.toLocalDate(dateTo), origin, destination);
        }
        return flightDtos;
    }
}
