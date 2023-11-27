package com.example.quoteoftheday.ui.favoritesscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.quoteoftheday.R
import com.example.quoteoftheday.model.FavoriteQuote
import java.time.LocalDateTime

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
                DateHeader(localDateTime = date)
            }
            items(favoritesForDate) {

            }
        }
    }

}

@Composable
fun DateHeader(
    localDateTime: LocalDateTime,
    modifier: Modifier = Modifier
) {

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