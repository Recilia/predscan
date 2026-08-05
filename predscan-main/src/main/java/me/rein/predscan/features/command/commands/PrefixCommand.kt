/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.command.commands

import me.rein.predscan.PredScan.commandManager
import me.rein.predscan.features.command.Command
import me.rein.predscan.file.FileManager.saveConfig
import me.rein.predscan.file.FileManager.valuesConfig

object PrefixCommand : Command("prefix") {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size <= 1) {
            chatSyntax("prefix <character>")
            return
        }

        val prefix = args[1]

        commandManager.prefix = prefix

        saveConfig(valuesConfig)

        chat("Successfully changed command prefix to '§8$prefix§3'")
    }
}