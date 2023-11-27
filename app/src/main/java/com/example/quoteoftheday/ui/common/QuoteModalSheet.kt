package com.example.quoteoftheday.ui.common

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.quoteoftheday.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteModalSheet(
    modifier: Modifier = Modifier,
    quoteText: String,
    currentNote: String = "",
    currentImageUri: Uri = Uri.EMPTY,
    saveQuote: (String, String, Uri) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        val scope = rememberCoroutineScope()
        val note = rememberSaveable {
            mutableStateOf(currentNote)
        }
        // Consult https://proandroiddev.com/jetpack-compose-new-photo-picker-b280950ba934
        val context = LocalContext.current
        var imageUri by rememberSaveable {
            mutableStateOf(currentImageUri)
        }
        val potentialPhotoUri = createImageUri(context)
        val photoPickLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = {uri ->
                if (uri != null) {
                    imageUri = uri
                }
            }
        )
        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                if (success) {
                    if (potentialPhotoUri != null) {
                        imageUri = potentialPhotoUri
                    }
                }
            }
        )
        Column {
            Text(
                text = stringResource(id = R.string.modal_sheet_title)
            )
            Divider()
            // Text area to write notes
            AttachmentArea(
                note = note,
                potentialPhotoUri = potentialPhotoUri,
                cameraLauncher = cameraLauncher,
                photoPickLauncher = photoPickLauncher
            )
            // Buttons to cancel or save
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismiss()
                        }
                    }
                }) {
                    Text(
                        text = stringResource(id = R.string.cancel_button)
                    )
                }
                Button(onClick = {
                    saveQuote(quoteText, note.value, imageUri)
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismiss()
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

@Composable
fun AttachmentArea(
    modifier: Modifier = Modifier,
    note: MutableState<String>,
    potentialPhotoUri: Uri?,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    photoPickLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
) {
    TextField(
        value = note.value,
        onValueChange = {note.value = it},
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
    // this IconButton could be embedded into the TextField
    Row(
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = {
            cameraLauncher.launch(potentialPhotoUri)
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