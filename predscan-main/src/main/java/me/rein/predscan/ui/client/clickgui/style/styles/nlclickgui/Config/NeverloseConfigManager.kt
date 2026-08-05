package me.rein.predscan.ui.client.clickgui.style.styles.nlclickgui.Config

import me.rein.predscan.PredScan
import me.rein.predscan.utils.client.ClientUtils
import java.io.File
import java.io.IOException
import java.util.Comparator

class NeverloseConfigManager {

    private val configs: MutableList<NeverloseConfig> = ArrayList()

    init {
        refresh()
    }

    fun getConfigs(): List<NeverloseConfig> {
        if (configs.isEmpty()) {
            refresh()
        }
        return configs
    }

    fun activeConfig(): NeverloseConfig? {
        val active = PredScan.fileManager.nowConfig
        return configs.firstOrNull { it.name.equals(active, ignoreCase = true) }
    }

    fun refresh() {
        configs.clear()
        val configFiles = PredScan.fileManager.settingsDir.listFiles { _, name ->
            name.endsWith(".json") || name.endsWith(".txt")
        }
        if (configFiles != null) {
            for (file in configFiles) {
                configs.add(NeverloseConfig(removeExtension(file.name), file))
            }
            configs.sortWith(Comparator.comparing({ it.name }, String.CASE_INSENSITIVE_ORDER))
        }
    }

    fun toggleExpansion(config: NeverloseConfig) {
        config.isExpanded = !config.isExpanded
    }

    fun loadConfig(name: String) {
        PredScan.fileManager.load(name, true)
        refresh()
    }

    fun saveConfig(name: String) {
        PredScan.fileManager.load(name, false)
        PredScan.fileManager.saveAllConfigs()
        refresh()
    }

    fun deleteConfig(config: NeverloseConfig) {
        val file = config.file
        if (file.exists() && !file.delete()) {
            ClientUtils.LOGGER.warn("Failed to delete config file: {}", file.name)
        }
        if (PredScan.fileManager.nowConfig == config.name) {
            PredScan.fileManager.load("default", false)
            PredScan.fileManager.saveAllConfigs()
        }
        refresh()
    }

    fun ensureConfig(name: String): NeverloseConfig {
        val file = File(PredScan.fileManager.settingsDir, "$name.json")
        if (!file.exists()) {
            try {
                file.createNewFile()
                PredScan.fileManager.load(name, false)
                PredScan.fileManager.saveAllConfigs()
            } catch (e: IOException) {
                ClientUtils.LOGGER.error("Failed to create config {}", name, e)
            }
            refresh()
        }
        return configs.firstOrNull { it.name.equals(name, ignoreCase = true) }
            ?: NeverloseConfig(name, file).also { configs.add(it) }
    }

    private fun removeExtension(name: String): String {
        val dotIndex = name.lastIndexOf('.')
        return if (dotIndex == -1) name else name.substring(0, dotIndex)
    }
}
