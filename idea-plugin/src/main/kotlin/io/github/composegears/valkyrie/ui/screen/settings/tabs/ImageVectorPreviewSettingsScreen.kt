package io.github.composegears.valkyrie.ui.screen.settings.tabs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composegears.tiamat.koin.koinSharedTiamatViewModel
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.dim
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateImageVectorPreview
import org.koin.compose.koinInject

val ImageVectorPreviewSettingsScreen by navDestination<Unit> {
    val inMemorySettings = koinInject<InMemorySettings>()
    val settings by inMemorySettings.settings.collectAsState()

    val settingsViewModel = koinSharedTiamatViewModel<SettingsViewModel>()

    ImageVectorPreviewSettingsUi(
        showImageVectorPreview = settings.showImageVectorPreview,
        onAction = settingsViewModel::onAction,
    )
}

@Composable
private fun ImageVectorPreviewSettingsUi(
    showImageVectorPreview: Boolean,
    modifier: Modifier = Modifier,
    onAction: (SettingsAction) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        VerticalSpacer(16.dp)

        ListItem(
            modifier = Modifier
                .toggleable(
                    value = showImageVectorPreview,
                    onValueChange = { onAction(UpdateImageVectorPreview(it)) },
                )
                .padding(horizontal = 8.dp)
                .heightIn(max = 100.dp),
            headlineContent = {
                Text(text = "Show ImageVector preview")
            },
            supportingContent = {
                Text(
                    text = "Enable icon preview functionality in the IDE without @Preview annotation",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = LocalContentColor.current.dim(),
                )
            },
            trailingContent = {
                Switch(
                    modifier = Modifier.scale(0.9f),
                    checked = showImageVectorPreview,
                    onCheckedChange = { onAction(UpdateImageVectorPreview(it)) },
                )
            },
        )
    }
}

@Preview
@Composable
private fun ImageVectorPreviewSettingsPreview() = PreviewTheme(alignment = Alignment.TopStart) {
    ImageVectorPreviewSettingsUi(showImageVectorPreview = true, onAction = {})
}
