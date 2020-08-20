package com.agro.agro.utils;

import androidx.room.TypeConverter;

import java.util.Date;

// Converter for DAO class.
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
