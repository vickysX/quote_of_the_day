package com.example.quoteoftheday.ui.quotescreen

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quoteoftheday.R
import kotlinx.coroutines.launch

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
                FavoriteQuoteSavingSheet(
                    dismiss = toggleSheetVisibility,
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

// TODO: To be refactored
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteQuoteSavingSheet(
    modifier: Modifier = Modifier,
    quoteText: String,
    dismiss: () -> Unit,
    saveQuote: (String, String, Uri) -> Unit,
    sheetState: SheetState,
    //successfulSaving: Boolean
) {
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = dismiss,
        sheetState = sheetState
    ) {
        /*val readImagesPermission = rememberPermissionState(
            android.Manifest.permission.READ_
        )
        val hasPermission = readImagesPermission.status.isGranted*/
        var note by rememberSaveable {
            mutableStateOf("")
        }
        Column {
            // Title: Save quote to favs
            Text(
                text = stringResource(id = R.string.modal_sheet_title)
            )
            Divider()
            // Text area to write notes
            TextField(
                value = note,
                onValueChange = {note = it},
                singleLine = false,
                minLines = 3,
                maxLines = 6,
                label = {
                    Text(
                        text = stringResource(id = R.string.notes_label)
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.notes_placeholder)
                    )
                }
            )
            // Consult https://proandroiddev.com/jetpack-compose-new-photo-picker-b280950ba934
            val context = LocalContext.current
            var imageUri by rememberSaveable {
                mutableStateOf(Uri.EMPTY)
            }
            val potentialPhotoUri = createImageUri(context)
            val photoPickLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
                onResult = {uri -> imageUri = uri}
            )
            val cameraLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicture(),
                onResult = { success ->
                    if (success) {
                        imageUri = potentialPhotoUri
                    }
                }
            )
            // this IconButton could be embedded into the TextField
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = {
                    cameraLauncher.launch(potentialPhotoUri)
                    //saveQuote(quoteText, note, newImageUri)
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            id = R.drawable.photo_camera
                        ),
                        contentDescription = stringResource(
                            id = R.string.take_photo_description
                        )
                    )
                }
                IconButton(onClick = {
                    photoPickLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                    //saveQuote(quoteText, note, selectedImageUri)
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            id = R.drawable.attach_photo
                        ),
                        contentDescription = stringResource(
                            id = R.string.pick_photo_description
                        )
                    )
                }
            }
            // Buttons to cancel or save
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            dismiss()
                        }
                    }
                }) {
                    Text(
                        text = stringResource(id = R.string.cancel_button)
                    )
                }
                Button(onClick = { 
                    saveQuote(quoteText, note, imageUri)
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            dismiss()
                        }
                    }
                }) {
                    Text(
                        text = stringResource(id = R.string.save_button)
                    )
                }
            }
        }
    }
}

private fun createImageUri(context: Context) : Uri? {
    val fileName = "quote_image_${System.currentTimeMillis()}.jpeg"
    val dirUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL_PRIMARY
        )
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
    val newImage = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }
    return context.contentResolver.insert(
        dirUri, newImage
    )
}
