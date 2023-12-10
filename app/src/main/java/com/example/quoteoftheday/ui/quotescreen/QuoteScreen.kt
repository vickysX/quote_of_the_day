package com.example.quoteoftheday.ui.quotescreen

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import com.example.quoteoftheday.R
import com.example.quoteoftheday.ui.common.QuoteModalSheet
import com.example.quoteoftheday.ui.theme.QuoteOfTheDayTheme

@Composable
fun QuoteScreen(
    modifier: Modifier = Modifier,
    viewModel: QuoteViewModel = hiltViewModel()
) {
    val todayQuoteUiState = viewModel.todayQuoteUiState.collectAsStateWithLifecycle()
    when (val state = todayQuoteUiState.value) {
        is TodayQuoteUiState.Loading -> LoadingScreen(modifier = modifier)
        is TodayQuoteUiState.Error -> ErrorScreen(modifier = modifier)
        is TodayQuoteUiState.FetchedQuoteComplete -> {
            val (quote, author, isAFavorite, imageRes) = state
            SuccessQuoteScreen(
                quote = quote,
                author = author,
                isAFavorite = isAFavorite,
                imageRes = imageRes,
                setUserPreferences = {viewModel.setUserPreference(it)},
                removeQuoteFromFavorites = { favQuote, favAuthor ->
                    viewModel.removeQuoteFromFavorites(favQuote, favAuthor)
                },
                addQuoteToFavorites = { favQuote, favAuthor, favNote, favImageUri ->
                    viewModel.addQuoteToFavorites(
                        favQuote, favAuthor, favNote, favImageUri
                    )
                },
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessQuoteScreen(
    modifier: Modifier = Modifier,
    quote: String,
    author: String,
    isAFavorite: Boolean,
    @DrawableRes imageRes: Int,
    setUserPreferences: (Boolean) -> Unit,
    removeQuoteFromFavorites: (String, String) -> Unit,
    addQuoteToFavorites: (String, String, String, Uri) -> Unit
    ) {
    var isSheetVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val toggleSheetVisibility = {
        isSheetVisible = !isSheetVisible
    }
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(
        context.resources,
        imageRes
    )
    val palette = Palette.from(bitmap).generate()
    val vibrant = palette.vibrantSwatch
    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
        // Wallpaper
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            // Text of the quote
            Text(
                text = quote,
                color = vibrant?.let {
                    Color(it.titleTextColor)
                } ?: Color.Unspecified,
                modifier = Modifier
            )
            // Author name
            Text(
                text = author,
                color = vibrant?.let {
                    Color(it.bodyTextColor)
                } ?: Color.Unspecified
            )
            // Share and Save buttons
            Row {
                IconButton(onClick = {
                    shareQuote(
                        quote = quote,
                        author = author,
                        context = context
                    )
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            id = R.drawable.baseline_share_24
                        ),
                        contentDescription = stringResource(
                            id = R.string.share_button
                        ),

                        )
                }
                IconButton(onClick = {
                    if (!isAFavorite) {
                        toggleSheetVisibility()
                    } else {
                        setUserPreferences(false)
                        removeQuoteFromFavorites(
                            quote, author
                        )
                    }

                }) {
                    if (!isAFavorite) {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                id = R.drawable.ic_favorite_outlined
                            ),
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                id = R.drawable.ic_favorite_filled
                            ),
                            contentDescription = null
                        )
                    }
                }
            }
            if (isSheetVisible) {
                QuoteModalSheet(
                    onDismiss = toggleSheetVisibility,
                    saveQuote = { quote: String, note: String, photoUri: Uri ->
                        addQuoteToFavorites(
                            quote, author, note, photoUri
                        )
                        toggleSheetVisibility()
                    },
                    sheetState = sheetState,
                    quoteText = quote
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {

}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier
) {

}

private fun shareQuote(quote: String, author: String, context: Context) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "\"$quote\"\n- $author"
        )
    }
    val shareIntent = Intent.createChooser(intent, null)
    context.startActivity(shareIntent)
}

@Preview
@Composable
fun QuoteScreenPreview() {
    QuoteOfTheDayTheme {
        QuoteScreen()
    }
}