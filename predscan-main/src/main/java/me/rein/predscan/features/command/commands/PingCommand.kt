/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.command.commands

import me.rein.predscan.features.command.Command

object PingCommand : Command("ping") {

    override fun execute(args: Array<String>) {
        chat("§3Your ping is §a${mc.netHandler.getPlayerInfo(mc.thePlayer.uniqueID).responseTime}ms§3.")
    }

}