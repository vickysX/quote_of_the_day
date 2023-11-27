package com.example.quoteoftheday.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "favorite_quotes")
data class FavoriteQuote(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    val quote: String,

    var note: String = "",

    var photoUri: Uri = Uri.EMPTY,

    val timestamp: LocalDateTime = LocalDateTime.now()
)
