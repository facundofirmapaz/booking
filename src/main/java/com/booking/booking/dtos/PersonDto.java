package com.booking.booking.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto
{
    private int dni;
    private String name;
    private String lastname;
    private String birthDate;
    private String mail;

}
