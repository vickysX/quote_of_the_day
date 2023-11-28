package com.example.quoteoftheday.ui.favoritesscreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteoftheday.data.FavoriteQuotesRepository
import com.example.quoteoftheday.model.FavoriteQuote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteQuotesRepository: FavoriteQuotesRepository
) : ViewModel() {

    private lateinit var grouped : Map<LocalDateTime, List<FavoriteQuote>>

    private val favorites : StateFlow<List<FavoriteQuote>?> =
        favoriteQuotesRepository.getFavoriteQuotes().map {
            it
        }
            .stateIn(
                scope = viewModelScope,
                initialValue = listOf(),
                started = SharingStarted.WhileSubscribed(5_000L)
            )

    val groupedFavorites = MutableStateFlow(grouped).asStateFlow()

    private fun groupFavorites() {
        grouped = favorites.value!!.groupBy {it.timestamp}
    }

    fun deleteFavQuote(quote: String, note: String, imageUri: Uri) {
        viewModelScope.launch {
            val favQuote = FavoriteQuote(
                quote = quote,
                note = note,
                photoUri = imageUri
            )
            favoriteQuotesRepository.deleteQuote(favQuote)
        }
    }

    fun updateFavQuote(quote: String, note: String, imageUri: Uri) {
        viewModelScope.launch {
            val favQuote = FavoriteQuote(
                quote = quote,
                note = note,
                photoUri = imageUri
            )
            favoriteQuotesRepository.updateQuote(favQuote)
        }
    }

    init {
        groupFavorites()
    }

}