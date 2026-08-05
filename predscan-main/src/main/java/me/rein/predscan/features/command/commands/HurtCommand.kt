/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.command.commands

import me.rein.predscan.features.command.Command
import me.rein.predscan.utils.client.PacketUtils.sendPacket
import me.rein.predscan.utils.client.PacketUtils.sendPackets
import me.rein.predscan.utils.extensions.component1
import me.rein.predscan.utils.extensions.component2
import me.rein.predscan.utils.extensions.component3
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

object HurtCommand : Command("hurt") {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        var damage = 1

        if (args.size > 1) {
            try {
                damage = args[1].toInt()
            } catch (ignored: NumberFormatException) {
                chatSyntaxError()
                return
            }
        }

        // Latest NoCheatPlus damage exploit
        val (x, y, z) = mc.thePlayer

        repeat(65 * damage) {
            sendPackets(
                C04PacketPlayerPosition(x, y + 0.049, z, false),
                C04PacketPlayerPosition(x, y, z, false)
            )
        }

        sendPacket(C04PacketPlayerPosition(x, y, z, true))

        // Output message
        chat("You were damaged.")
    }
}