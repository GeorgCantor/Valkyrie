package io.github.composegears.valkyrie.settings

import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import io.github.composegears.valkyrie.settings.PersistentSettings.ValkyrieState
import io.github.composegears.valkyrie.ui.screen.intro.Mode

@Service
@State(name = "Valkyrie.Settings", storages = [Storage("valkyrie_settings.xml")])
class PersistentSettings : SimplePersistentStateComponent<ValkyrieState>(ValkyrieState()) {

    class ValkyrieState : BaseState() {
        var mode by string(Mode.Unspecified.name)

        var packageName by string()
        var iconPackName by string()

        var nestedPacks by string()
        var currentNestedPack by string()

        var generatePreview by property(false)

        var initialDirectory by string()
    }

    companion object {
        @JvmStatic
        val persistentSettings = service<PersistentSettings>().state
    }
}