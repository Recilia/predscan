/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.handler.macro

import me.rein.predscan.PredScan.commandManager

class Macro(val key: Int, val command: String) {
    fun exec() {
        commandManager.executeCommands(command)
    }
}