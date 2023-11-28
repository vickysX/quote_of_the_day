package com.example.quoteoftheday.ui.favoritesscreen

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quoteoftheday.R
import com.example.quoteoftheday.model.FavoriteQuote
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    onGoingBack: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val groupedFavorites = viewModel.groupedFavorites.collectAsState()
    BackHandler {
        onGoingBack()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.favorites_screen)
                    )
                }
            )
        },
    ) { scaffoldPaddingValues ->
        FavoritesList(
            grouped = groupedFavorites.value,
            modifier = Modifier.padding(scaffoldPaddingValues),
            onUpdateQuote = { quote: String, note: String, imageUri: Uri ->
                viewModel.updateFavQuote(quote, note, imageUri)
            },
            onDeleteQuote = { quote: String, note: String, imageUri: Uri ->
                viewModel.deleteFavQuote(quote, note, imageUri)
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoritesList(
    //favorites: List<FavoriteQuote>,
    modifier: Modifier = Modifier,
    grouped: Map<LocalDateTime, List<FavoriteQuote>>,
    onDeleteQuote: (String, String, Uri) -> Unit,
    onUpdateQuote: (String, String, Uri) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        grouped.forEach { (date, favoritesForDate) ->
            stickyHeader {
                DateHeader(date = date)
            }
            items(favoritesForDate) {favoriteQuote ->
                FavoriteQuoteItem(
                    item = favoriteQuote,
                    onDeleteQuote = onDeleteQuote,
                    onUpdateQuote = onUpdateQuote
                )
            }
        }
    }

}

@Composable
fun DateHeader(
    modifier: Modifier = Modifier,
    date: LocalDateTime,
) {
    val today = LocalDateTime.now()
    val duration = Duration.between(date, today)
    val text = when {
        duration.toDays() < 1L -> stringResource(id = R.string.today)
        duration.toDays() in 1L..7L -> stringResource(id = R.string.this_week)
        duration.toDays() < 30L -> stringResource(id = R.string.last_month)
        duration.get(ChronoUnit.MONTHS) in 1L..2L -> stringResource(id = R.string.one_month)
        duration.get(ChronoUnit.MONTHS) in 2L..6L -> stringResource(id = R.string.last_semester)
        duration.get(ChronoUnit.MONTHS) in 7L..20L -> stringResource(id = R.string.one_year)
        else -> stringResource(id = R.string.years_ago)
    }
    Surface {
        Text(text = text)
    }
}

@Composable
fun FavoriteQuoteItem(
    modifier: Modifier = Modifier,
    item: FavoriteQuote,
    onUpdateQuote: (String, String, Uri) -> Unit,
    onDeleteQuote: (String, String, Uri) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            )
    ) {
        Divider()
    }
}

@Composable
fun ItemMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismiss: () -> Unit,
) {
    /*var isSheetVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val toggleSheetVisibility = {
        isSheetVisible = !isSheetVisible
    }*/
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss) {
        DropdownMenuItem(
            text = { 
                Text(
                    text = stringResource(id = R.string.review_fav)
                )
            }, 
            onClick = { /*TODO*/ }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(id = R.string.delete_fav)
                )
            }, 
            onClick = { /*TODO*/ }
        )
    }
}