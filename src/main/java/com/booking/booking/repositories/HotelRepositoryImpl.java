package com.booking.booking.repositories;

import com.booking.booking.dtos.HotelDto;
import com.booking.booking.dtos.ReservDto;
import com.booking.booking.exceptionsHandlers.HotelNotFoundException;
import com.booking.booking.utils.DateUtils;
import com.booking.booking.utils.DbUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class HotelRepositoryImpl implements HotelRepository
{

    private static Map<String, ReservDto> reservas = new HashMap<>();

    private final DbUtils dbUtils;

    public HotelRepositoryImpl(DbUtils dbUtils)
    {
        this.dbUtils = dbUtils;
    }

    @Override
    public ArrayList<HotelDto> getAll()
    {
        ArrayList<HotelDto> hoteles = new ArrayList<>();

        var data = dbUtils.loadHotelsDataBase("classpath:dbHotels.json");

        if (data != null)
            hoteles = data;

        return hoteles;
    }

    @Override
    public boolean validateDestination(String destination) {

        ArrayList<HotelDto> hoteles = new ArrayList<>();
        var data = dbUtils.loadHotelsDataBase("classpath:dbHotels.json");

        if (data != null)
            hoteles = data;

        var hotel = hoteles.stream().filter(h -> h.getCity().equals(destination)).findFirst();

        return hotel.isPresent();
    }

    @Override
    public boolean hotelIsFreeInRange(LocalDate dateFrom, LocalDate dateTo, HotelDto hotel)
    {
        LocalDate hotelFreeFrom =  DateUtils.toLocalDate(hotel.getDateFrom());
        LocalDate hotelFreeTo = DateUtils.toLocalDate(hotel.getDateTo());

        return hotelFreeFrom.compareTo(dateFrom) <= 0 &&
                dateTo.compareTo(hotelFreeTo) <= 0 &&
                !reservas.containsKey(hotel.getHotelCode());
    }

    @Override
    public ArrayList<HotelDto> getByRangeAndDestination(LocalDate dateFrom, LocalDate dateTo, String destination)
    {
        ArrayList<HotelDto> hoteles = new ArrayList<>();
        var data = dbUtils.loadHotelsDataBase("classpath:dbHotels.json");

        if (data != null)
            hoteles = data;

        var resp = hoteles.stream()
                                         .filter(h -> h.getCity().equals(destination) && hotelIsFreeInRange(dateFrom, dateTo, h))
                                         .collect(Collectors.toList());

        return (ArrayList<HotelDto>) resp;
    }

    @SneakyThrows
    @Override
    public HotelDto getById(String hotelCode)
    {
        var data = dbUtils.loadHotelsDataBase("classpath:dbHotels.json");
        HotelDto hotelDto = null;

        if (data != null) {
            for (HotelDto h : data) {
                if (h.getHotelCode().equals(hotelCode)) {
                    hotelDto = h;
                    break;
                }
            }
        }

        if(hotelDto == null)
            throw new HotelNotFoundException("Hotel no encontrado: " + hotelCode);

        return hotelDto;
    }

    @Override
    public void addReserva(ReservDto resp) {
        reservas.put(resp.getBooking().getHotelCode(), resp);
    }
}
