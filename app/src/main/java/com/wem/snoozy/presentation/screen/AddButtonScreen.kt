package com.wem.snoozy.presentation.screen

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wem.snoozy.data.local.UserPreferencesManager
import com.wem.snoozy.presentation.viewModel.AddAlarmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetScreen(
    context: Context = LocalContext.current.applicationContext,
    viewModel: AddAlarmViewModel = viewModel {
        AddAlarmViewModel(UserPreferencesManager(context))
    },
    onDismiss: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = {
//            showBottomSheet = false
//                    viewModel.resetStates()
        },
        dragHandle = {
            Box(modifier = Modifier.height(0.dp))
        },
        sheetGesturesEnabled = false,
        containerColor = MaterialTheme.colorScheme.surface,
        scrimColor = Color.Black.copy(.85f)
    ) {
        BottomSheetContent {
//            showBottomSheet = false
//                    viewModel.resetStates()
        }
    }
}