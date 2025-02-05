package io.github.composegears.valkyrie.ui.screen.intro

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composegears.tiamat.NavDestination
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import io.github.composegears.valkyrie.ui.domain.model.Mode
import io.github.composegears.valkyrie.ui.domain.model.Mode.IconPack
import io.github.composegears.valkyrie.ui.domain.model.Mode.Simple
import io.github.composegears.valkyrie.ui.domain.model.Mode.Unspecified
import io.github.composegears.valkyrie.ui.foundation.IconButton
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.WeightSpacer
import io.github.composegears.valkyrie.ui.foundation.dim
import io.github.composegears.valkyrie.ui.foundation.icons.BatchProcessing
import io.github.composegears.valkyrie.ui.foundation.icons.Settings
import io.github.composegears.valkyrie.ui.foundation.icons.SimpleConversion
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.intro.util.rememberPluginVersion
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.IconPackCreationScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.setup.SimpleModeSetupScreen
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen

val IntroScreen: NavDestination<Unit> by navDestination {

    val navController = navController()

    IntroScreenUI(
        openSettings = {
            navController.navigate(
                dest = SettingsScreen,
                transition = navigationSlideInOut(true),
            )
        },
        onModeChange = {
            when (it) {
                Simple -> {
                    navController.navigate(
                        dest = SimpleModeSetupScreen,
                        transition = navigationSlideInOut(true),
                    )
                }
                IconPack -> {
                    navController.navigate(
                        dest = IconPackCreationScreen,
                        transition = navigationSlideInOut(true),
                    )
                }
                Unspecified -> {}
            }
        },
    )
}

@Composable
private fun IntroScreenUI(
    openSettings: () -> Unit,
    onModeChange: (Mode) -> Unit,
) {
    Box {
        IconButton(
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.TopEnd),
            imageVector = ValkyrieIcons.Settings,
            iconSize = 24.dp,
            onClick = openSettings,
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WeightSpacer(weight = 0.3f)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Welcome to Valkyrie",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
                VerticalSpacer(42.dp)
                Text(
                    text = "Choose conversion mode",
                    style = MaterialTheme.typography.labelSmall,
                    color = LocalContentColor.current.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                )
                VerticalSpacer(8.dp)

                SelectableCard(
                    onClick = { onModeChange(Simple) },
                    image = ValkyrieIcons.SimpleConversion,
                    title = "Simple",
                    description = "One-click conversion from SVG/XML into ImageVector",
                )
                VerticalSpacer(16.dp)
                SelectableCard(
                    onClick = { onModeChange(IconPack) },
                    image = ValkyrieIcons.BatchProcessing,
                    title = "IconPack",
                    description = "Create organized icon pack with batch export into your project",
                )
            }
            WeightSpacer(weight = 0.7f)
        }
        Text(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.labelSmall,
            text = rememberPluginVersion(),
        )
    }
}

@Composable
private fun SelectableCard(
    onClick: () -> Unit,
    image: ImageVector,
    title: String,
    description: String,
) {
    Card(
        onClick = onClick,
        elevation = CardDefaults.elevatedCardElevation(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Top)
                    .size(36.dp),
                imageVector = image,
                contentDescription = null,
            )
            Column(
                modifier = Modifier.width(250.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = LocalContentColor.current.dim(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun IntroScreenUIPreview() = PreviewTheme {
    IntroScreenUI(
        openSettings = {},
        onModeChange = {},
    )
}
