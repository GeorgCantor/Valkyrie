package io.github.composegears.valkyrie.ui.screen.mode.iconpack.destination

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.DragAndDropBox
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.WeightSpacer
import io.github.composegears.valkyrie.ui.foundation.icons.Folder
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.rememberFolderDragAndDropHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.IconPackCreationScreen

val IconPackDestinationScreen by navDestination<Unit> {
    val navController = navController()
    val viewModel = koinTiamatViewModel<IconPackDestinationViewModel>()

    val state by viewModel.state.collectAsState()

    val dragAndDropHandler = rememberFolderDragAndDropHandler(onDrop = viewModel::updateDestination)
    val isDragging by remember(dragAndDropHandler.isDragging) { mutableStateOf(dragAndDropHandler.isDragging) }

    var showDirectoryPicker by remember { mutableStateOf(false) }

    IconPackDestinationScreenUI(
        state = state,
        isDragging = isDragging,
        onChooseDirectory = { showDirectoryPicker = true },
        onBack = navController::back,
        onNext = {
            viewModel.saveSettings()
            navController.navigate(IconPackCreationScreen)
        }
    )

    DirectoryPicker(show = showDirectoryPicker) {
        showDirectoryPicker = false
        if (it != null) {
            viewModel.updateDestination(it)
        }
    }
}

@Composable
private fun IconPackDestinationScreenUI(
    state: IconPackDestinationState,
    isDragging: Boolean,
    onChooseDirectory: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar {
            BackAction(onBack = onBack)
            AppBarTitle(title = "IconPack destination")
        }
        VerticalSpacer(36.dp)
        DragAndDropBox(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            isDragging = isDragging,
            onChoose = onChooseDirectory
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = ValkyrieIcons.Folder,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center,
                    text = "Drag & Drop folder\nor browse",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
        VerticalSpacer(36.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (state.destination.isNotEmpty()) {
                    Text(
                        modifier = Modifier.align(Alignment.Start),
                        text = "Export path:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        modifier = Modifier.align(Alignment.Start),
                        text = state.destination,
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.bodySmall
                    )
                    VerticalSpacer(36.dp)
                }
                Button(
                    modifier = Modifier.align(Alignment.End),
                    enabled = state.nextButtonEnabled,
                    onClick = onNext,
                ) {
                    Text(text = "Next")
                }
            }
        }
        WeightSpacer()
    }
}

@Preview
@Composable
private fun IconPackDestinationScreenPreview() {
    IconPackDestinationScreenUI(
        state = IconPackDestinationState(
            nextButtonEnabled = true,
            destination = "Users/Downloads/IconPackDestination"
        ),
        onChooseDirectory = {},
        isDragging = false,
        onBack = {},
        onNext = {},
    )
}