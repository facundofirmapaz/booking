package com.booking.booking.controllers;

import com.booking.booking.dtos.HotelDto;
import com.booking.booking.dtos.NewReservDto;
import com.booking.booking.dtos.ReservDto;
import com.booking.booking.services.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HotelController extends BaseController
{
    private final HotelService hotelService;

    public HotelController(HotelService hotelService)
    {
        this.hotelService = hotelService;
    }

    @GetMapping("/hotels")
    public ResponseEntity<?> get(@RequestParam Map<String,String> params)
    {
        ArrayList<HotelDto> resp = hotelService.get(params);

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/booking")
    public ResponseEntity<?> addReserv(@RequestBody NewReservDto newReservDto)
    {
        ReservDto reservDto = hotelService.newReserv(newReservDto);

        return ResponseEntity.ok(reservDto);
    }
}
