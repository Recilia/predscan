/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.command.commands

import me.rein.predscan.features.command.Command
import me.rein.predscan.utils.io.MiscUtils
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

object UsernameCommand : Command("username", "ign") {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        val username = mc.thePlayer.name

        chat("Username: $username")

        MiscUtils.copy(username)
    }
}