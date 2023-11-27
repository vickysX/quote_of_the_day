package com.example.quoteoftheday.ui.favoritesscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.quoteoftheday.R
import com.example.quoteoftheday.model.FavoriteQuote
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier
) {}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoritesList(
    favorites: List<FavoriteQuote>,
    grouped: Map<LocalDateTime, List<FavoriteQuote>>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        grouped.forEach { (date, favoritesForDate) ->
            stickyHeader {
                DateHeader(date = date)
            }
            items(favoritesForDate) {

            }
        }
    }

}

@Composable
fun DateHeader(
    date: LocalDateTime,
    modifier: Modifier = Modifier
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
    item: FavoriteQuote,
    modifier: Modifier = Modifier
) {
    Column() {

    }
}

@Composable
fun ItemMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
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