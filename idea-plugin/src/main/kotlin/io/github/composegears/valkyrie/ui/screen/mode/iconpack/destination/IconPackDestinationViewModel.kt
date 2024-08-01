package io.github.composegears.valkyrie.ui.screen.mode.iconpack.destination

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.parser.PackageExtractor
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.extension.updateState
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.invariantSeparatorsPathString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class IconPackDestinationViewModel(
    private val inMemorySettings: InMemorySettings,
) : TiamatViewModel() {

    private val settings = inMemorySettings.current

    private val _state = MutableStateFlow(
        IconPackDestinationState(
            nextButtonEnabled = settings.iconPackDestination.isNotEmpty(),
            iconPackDestination = settings.iconPackDestination,
            predictedPackage = PackageExtractor.getFrom(path = settings.iconPackDestination).orEmpty(),
        ),
    )
    val state = _state.asStateFlow()

    fun updateDestination(path: Path) {
        _state.updateState {
            copy(
                iconPackDestination = path.absolutePathString(),
                nextButtonEnabled = true,
                predictedPackage = PackageExtractor.getFrom(path.invariantSeparatorsPathString).orEmpty(),
            )
        }
    }

    fun saveSettings() {
        inMemorySettings.updateIconPackDestination(state.value.iconPackDestination)
    }
}

data class IconPackDestinationState(
    val nextButtonEnabled: Boolean = false,
    val iconPackDestination: String = "",
    val predictedPackage: String = "",
)
