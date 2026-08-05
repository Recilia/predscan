/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.command.commands

import me.rein.predscan.PredScan.isStarting
import me.rein.predscan.PredScan.moduleManager
import me.rein.predscan.features.command.Command
import me.rein.predscan.features.command.CommandManager
import me.rein.predscan.file.FileManager.accountsConfig
import me.rein.predscan.file.FileManager.clickGuiConfig
import me.rein.predscan.file.FileManager.friendsConfig
import me.rein.predscan.file.FileManager.hudConfig
import me.rein.predscan.file.FileManager.loadConfig
import me.rein.predscan.file.FileManager.modulesConfig
import me.rein.predscan.file.FileManager.valuesConfig
import me.rein.predscan.script.ScriptManager.disableScripts
import me.rein.predscan.script.ScriptManager.reloadScripts
import me.rein.predscan.script.ScriptManager.unloadScripts
import me.rein.predscan.ui.font.Fonts

object ReloadCommand : Command("reload", "configreload") {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        chat("Reloading...")
        isStarting = true

        chat("§c§lReloading commands...")
        CommandManager.registerCommands()

        disableScripts()
        unloadScripts()

        for (module in moduleManager)
            moduleManager.generateCommand(module)

        chat("§c§lReloading scripts...")
        reloadScripts()

        chat("§c§lReloading fonts...")
        Fonts.loadFonts()

        chat("§c§lReloading modules...")
        loadConfig(modulesConfig)


        chat("§c§lReloading values...")
        loadConfig(valuesConfig)

        chat("§c§lReloading accounts...")
        loadConfig(accountsConfig)

        chat("§c§lReloading friends...")
        loadConfig(friendsConfig)

        chat("§c§lReloading HUD...")
        loadConfig(hudConfig)

        chat("§c§lReloading ClickGUI...")
        loadConfig(clickGuiConfig)

        isStarting = false
        chat("Reloaded.")
    }
}
