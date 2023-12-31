package com.example.quoteoftheday.data

import android.net.Uri
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ProvidedTypeConverter
class Converters {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    @TypeConverter
    fun dateToTimestamp(dateTime: LocalDateTime) : String {
        return dateTime.format(formatter)
    }

    @TypeConverter
    fun timestampToDate(timestamp: String) : LocalDateTime {
        return LocalDateTime.parse(timestamp, formatter)
    }

    @TypeConverter
    fun uriToString(uri: Uri) : String {
        return uri.toString()
    }

    @TypeConverter
    fun stringToUri(uriString: String) : Uri {
        return Uri.parse(uriString) ?: Uri.EMPTY
    }
}