package com.example.quoteoftheday.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quoteoftheday.R
import com.example.quoteoftheday.ui.favoritesscreen.FavoritesScreen
import com.example.quoteoftheday.ui.quotescreen.QuoteScreen
import com.example.quoteoftheday.ui.theme.QuoteOfTheDayTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun QuotesApp(
    modifier: Modifier = Modifier,
    viewModel: QuoteOfTheDayViewModel = viewModel()
) {
    // TODO: Consider changing method for asking permission
    // TODO: This is basic permission handling
    // try to study better solutions
    val permissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS
    )
    LaunchedEffect(permissionState) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }
    val uiState = viewModel.uiState.collectAsState()
    val currentTab = uiState.value.currentTab
    Scaffold(
        /*topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.app_name)
                    )
                },
            )
        },*/
        bottomBar = {
            QuotesNavBar(
                onTabPressed = { viewModel.setTab(it) },
                currentTab = currentTab
            )
        },
    ) {
        when (currentTab) {
            QuotesTabs.Today -> QuoteScreen(
                modifier = Modifier.padding(it),
            )

            else -> FavoritesScreen(
                modifier = Modifier.padding(it),
                onGoingBack = {viewModel.setTab(QuotesTabs.Today)}
            )
        }
    }

}

@Composable
private fun QuotesNavBar(
    modifier: Modifier = Modifier,
    onTabPressed: (QuotesTabs) -> Unit,
    currentTab: QuotesTabs
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentTab == QuotesTabs.Today,
            onClick = { onTabPressed(QuotesTabs.Today) },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.water_lux
                    ),
                    contentDescription = stringResource(id = R.string.today_tab)
                )
            },
            label = {
                Text(stringResource(id = R.string.today_label))
            }
        )
        NavigationBarItem(
            selected = currentTab == QuotesTabs.Past,
            onClick = { onTabPressed(QuotesTabs.Past) },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = stringResource(id = R.string.past_tab)
                )
            },
            label = {
                Text(stringResource(id = R.string.favorites_label))
            }
        )
    }
}

@Composable
@Preview
fun QuotesNavBarPreview() {
    QuoteOfTheDayTheme {
        QuotesNavBar(
            onTabPressed = {},
            currentTab = QuotesTabs.Today
        )
    }
}