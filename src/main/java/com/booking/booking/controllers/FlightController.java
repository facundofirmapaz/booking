package com.booking.booking.controllers;

import com.booking.booking.dtos.FlightDto;
import com.booking.booking.services.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class FlightController extends BaseController
{
    private final FlightService flightService;

    public FlightController(FlightService flightService)
    {
        this.flightService = flightService;
    }

    @GetMapping("/flights")
    public ResponseEntity<?> get(@RequestParam Map<String,String> params)
    {
        ArrayList<FlightDto> resp = flightService.get(params);

        return ResponseEntity.ok(resp);
    }
}
