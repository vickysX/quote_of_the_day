package com.example.quoteoftheday.ui.quotescreen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quoteoftheday.ui.common.QuoteModalSheet
import com.example.quoteoftheday.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen(
    modifier: Modifier = Modifier,
    viewModel: QuoteViewModel = hiltViewModel()
) {
    var isSheetVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val toggleSheetVisibility = {
        isSheetVisible = !isSheetVisible
    }
    val quoteUiState = viewModel.todayQuoteUiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    //val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        /*snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }*/
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)) {
            // Wallpaper
            Image(
                painter = painterResource(id = quoteUiState.value.imageRes),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                // Text of the quote
                Text(text = quoteUiState.value.quote)
                // Author name
                Text(text = quoteUiState.value.author)
                // Share and Save buttons
                Row {
                    IconButton(onClick = { /*TODO: Add functionality for sharing*/ }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                id = R.drawable.baseline_share_24
                            ),
                            contentDescription = stringResource(
                                id = R.string.share_button
                            )
                        )
                    }
                    IconButton(onClick = toggleSheetVisibility) {
                        // TODO: Add icon based on preference
                    }
                }
                if (isSheetVisible) {
                    QuoteModalSheet(
                        onDismiss = toggleSheetVisibility,
                        saveQuote = { quote: String, note: String, photoUri: Uri ->
                            viewModel.addQuoteToFavorites(
                                quote = quote,
                                note = note,
                                photoUri = photoUri
                            )
                            toggleSheetVisibility()
                        },
                        sheetState = sheetState,
                        quoteText = quoteUiState.value.quote
                    )
                }
            }
        }
    }
}
