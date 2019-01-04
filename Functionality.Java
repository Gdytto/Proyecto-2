package com.daniel.tods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//Clase para obtener los datos y poder ser actualizados
public class Functionality {

    public static String DateString(String Seconds, String formatString) {
        Date update = new Date(Long.parseLong(Seconds));
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(update);
    }
    public static Calendar Calender(String Seconds) {
        Date update = new Date(Long.parseLong(Seconds));
        Calendar cal = Calendar.getInstance();
        cal.setTime(update);
        return cal;
    }
}
