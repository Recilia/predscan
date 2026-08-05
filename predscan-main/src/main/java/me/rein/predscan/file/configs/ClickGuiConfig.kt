/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.file.configs

import com.google.gson.JsonObject
import me.rein.predscan.PredScan.clickGui
import me.rein.predscan.file.FileConfig
import me.rein.predscan.file.FileManager.PRETTY_GSON
import me.rein.predscan.ui.client.clickgui.ClickGui
import me.rein.predscan.utils.client.ClientUtils.LOGGER
import me.rein.predscan.file.gson.json
import me.rein.predscan.utils.io.readJson
import java.io.*

class ClickGuiConfig(file: File) : FileConfig(file) {

    override fun loadDefault() = ClickGui.setDefault()

    /**
     * Load config from file
     *
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun loadConfig() {
        // Regenerate panels and elements in case a script got loaded or removed.
        loadDefault()

        val json = file.readJson() as? JsonObject ?: return
        for (panel in clickGui.panels) {
            try {
                val panelObject = json.getAsJsonObject(panel.name)
                panel.open = panelObject["open"].asBoolean
                panel.isVisible = panelObject["visible"].asBoolean
                panel.x = panelObject["posX"].asInt
                panel.y = panelObject["posY"].asInt

            } catch (e: Exception) {
                LOGGER.error("Error while loading clickgui panel with the name '" + panel.name + "'.", e)
            }
        }
    }

    /**
     * Save config to file
     *
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun saveConfig() {
        val jsonObject = json {
            for (panel in clickGui.panels) {
                panel.name to json {
                    "open" to panel.open
                    "visible" to panel.isVisible
                    "posX" to panel.x
                    "posY" to panel.y
                }
            }
        }

        file.writeText(PRETTY_GSON.toJson(jsonObject))
    }
}