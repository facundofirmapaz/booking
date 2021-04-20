package com.booking.booking.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class DateUtils
{
    public static boolean validFormat(String value)
    {
        try
        {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(value, format);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    public static boolean validRange(String dateFrom, String dateTo)
    {
        var df = toLocalDate(dateFrom);
        var dt = toLocalDate(dateTo);

        return df.compareTo(dt) < 0;
    }

    public static LocalDate toLocalDate(String date)
    {
        LocalDate resp = null;

        if(validFormat(date))
        {
            var items = date.split("/");
            int d = Integer.parseInt(items[0]);
            int m = Integer.parseInt(items[1]);
            int y = Integer.parseInt(items[2]);
            resp = LocalDate.of(y, m, d);
        }

        return resp;
    }
}
