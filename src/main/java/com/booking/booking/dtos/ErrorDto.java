package com.booking.booking.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorDto
{
    private String type;
    private String message;
}

