package com.example.room.Model;

import androidx.room.TypeConverter;
import java.util.Date;

public class Converters {

    //Esto es copiado de chatgpt, es un convertidor del formato de la fecha.
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}

