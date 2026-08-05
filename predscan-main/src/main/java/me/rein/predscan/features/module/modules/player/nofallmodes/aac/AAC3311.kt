package me.rein.predscan.features.module.modules.player.nofallmodes.aac

import me.rein.predscan.features.module.modules.player.nofallmodes.NoFallMode
import me.rein.predscan.utils.movement.MovementUtils.serverOnGround
import me.rein.predscan.utils.client.PacketUtils.sendPackets
import me.rein.predscan.utils.extensions.stopXZ
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

object AAC3311 : NoFallMode("AAC3.3.11") {
    override fun onUpdate() {
        val thePlayer = mc.thePlayer

        if (thePlayer.fallDistance > 2) {
            thePlayer.stopXZ()

            sendPackets(
                C04PacketPlayerPosition(thePlayer.posX, thePlayer.posY - 10E-4, thePlayer.posZ, serverOnGround),
                C03PacketPlayer(true)
            )
        }
    }
}