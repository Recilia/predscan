/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.command.commands

import me.rein.predscan.features.command.Command
import me.rein.predscan.features.module.modules.exploit.Plugins

object PluginsCommand : Command("plugins") {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        Plugins.toggle()
    }
}