package com.example.quoteoftheday.ui.quotescreen

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quoteoftheday.ui.common.QuoteModalSheet

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
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            IconButton(onClick = toggleSheetVisibility) {
                
            }
            if (isSheetVisible) {
                QuoteModalSheet(
                    onDismiss = toggleSheetVisibility,
                    saveQuote = { quote: String, note: String, photoUri: Uri ->
                        viewModel.addQuoteToFavorites(quote, note, photoUri)
                        toggleSheetVisibility()
                    },
                    sheetState = sheetState,
                    quoteText = quoteUiState.value.quote
                )
            }
        }
    }
}
