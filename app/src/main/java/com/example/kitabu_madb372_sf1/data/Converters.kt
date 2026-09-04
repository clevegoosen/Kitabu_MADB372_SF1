package com.example.kitabu_madb372_sf1.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStatus(status: BookingStatus): String {
        return status.name
    }

    @TypeConverter
    fun toStatus(statusName: String): BookingStatus {
        return BookingStatus.valueOf(statusName)
    }
}
