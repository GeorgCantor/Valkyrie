package io.github.composegears.valkyrie.ui.screen.settings.tabs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composegears.tiamat.NavController
import com.composegears.tiamat.koin.koinSharedTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.domain.model.Mode
import io.github.composegears.valkyrie.ui.domain.model.Mode.IconPack
import io.github.composegears.valkyrie.ui.domain.model.Mode.Simple
import io.github.composegears.valkyrie.ui.domain.model.Mode.Unspecified
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.dim
import io.github.composegears.valkyrie.ui.foundation.disabled
import io.github.composegears.valkyrie.ui.foundation.icons.PlayForward
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.intro.IntroScreen
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import org.koin.compose.koinInject

val GeneralSettingsScreen by navDestination<Unit> {
    val navController = navController()

    val inMemorySettings = koinInject<InMemorySettings>()
    val settings by inMemorySettings.settings.collectAsState()

    val settingsViewModel = koinSharedTiamatViewModel<SettingsViewModel>()

    var showClearSettingsDialog by rememberMutableState { false }

    GeneralSettingsUi(
        mode = settings.mode,
        onClearSettings = {
            showClearSettingsDialog = true
        },
        onChangeMode = {
            settingsViewModel.resetMode()
            openIntro(navController)
        },
    )

    if (showClearSettingsDialog) {
        ClearSettingsDialog(
            onClear = {
                settingsViewModel.clearSettings()
                showClearSettingsDialog = false

                openIntro(navController)
            },
            onCancel = { showClearSettingsDialog = false },
        )
    }
}

private fun openIntro(navController: NavController) {
    navController.parent?.run {
        editBackStack { clear() }
        replace(IntroScreen)
    }
}

@Composable
private fun GeneralSettingsUi(
    mode: Mode,
    onClearSettings: () -> Unit,
    modifier: Modifier = Modifier,
    onChangeMode: () -> Unit,
) {
    val initialMode = remember { mode }
    val currentMode = remember(mode) { if (mode == Unspecified) initialMode else mode }

    Column(modifier = modifier.fillMaxWidth()) {
        VerticalSpacer(16.dp)
        ListItem(
            modifier = Modifier
                .clickable(
                    onClick = {
                        if (mode != Unspecified) onChangeMode()
                    },
                )
                .padding(horizontal = 8.dp),
            headlineContent = {
                val name = when (currentMode) {
                    Simple -> "Simple"
                    IconPack -> "IconPack"
                    Unspecified -> "Unspecified"
                }
                Text(text = "Current mode: $name")
            },
            supportingContent = {
                Text(
                    text = "Will be opened by default after the plugin relaunch",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = LocalContentColor.current.dim(),
                )
            },
            trailingContent = {
                val tint = if (mode == Unspecified) LocalContentColor.current.disabled() else LocalContentColor.current
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onChangeMode, enabled = mode != Unspecified)
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Change",
                        color = tint,
                    )
                    Icon(
                        imageVector = ValkyrieIcons.PlayForward,
                        tint = tint,
                        contentDescription = null,
                    )
                }
            },
        )
        VerticalSpacer(24.dp)
        SectionTitle(name = "Danger zone")
        TextButton(
            modifier = Modifier.padding(horizontal = 12.dp),
            colors = ButtonDefaults.textButtonColors().copy(
                contentColor = MaterialTheme.colorScheme.error,
            ),
            onClick = onClearSettings,
        ) {
            Text(text = "Clear all plugin settings")
        }
    }
}

@Composable
private fun SectionTitle(
    name: String,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
) {
    Text(
        modifier = modifier.padding(paddingValues),
        text = name,
        color = MaterialTheme.colorScheme.onSurfaceVariant.dim(),
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Normal,
        ),
    )
}

@Composable
private fun ClearSettingsDialog(
    onClear: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancel,
        shape = MaterialTheme.shapes.extraSmall,
        tonalElevation = 0.dp,
        title = {
            Text("Reset settings?")
        },
        text = {
            Text("This will reset plugin preferences back to their default settings and redirect to the start screen.")
        },
        textContentColor = MaterialTheme.colorScheme.onSurface,
        confirmButton = {
            Button(onClick = onClear) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel,
                colors = ButtonDefaults.textButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            ) {
                Text("Cancel")
            }
        },
    )
}

@Preview
@Composable
private fun GeneralSettingsPreview() = PreviewTheme(alignment = Alignment.TopStart) {
    GeneralSettingsUi(
        mode = Unspecified,
        onChangeMode = {},
        onClearSettings = {},
    )
}