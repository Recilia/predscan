/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.speedmodes.vulcan

import me.rein.predscan.event.JumpEvent
import me.rein.predscan.event.PacketEvent
import me.rein.predscan.features.module.modules.movement.speedmodes.SpeedMode
import me.rein.predscan.utils.extensions.isInLiquid
import me.rein.predscan.utils.movement.MovementUtils.strafe
import me.rein.predscan.utils.extensions.isMoving
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.potion.Potion

object VulcanGround288 : SpeedMode("VulcanGround288") {
    override fun onUpdate() {
        val player = mc.thePlayer ?: return
        if (player.isInLiquid || player.isInWeb || player.isOnLadder) return

        if (player.isMoving && collidesBottom()) {
            val speedEffect = player.getActivePotionEffect(Potion.moveSpeed)
            val isAffectedBySpeed = speedEffect != null && speedEffect.amplifier > 0
            val isMovingSideways = player.moveStrafing != 0f

            val strafe = when {
                isAffectedBySpeed -> 0.59f
                isMovingSideways -> 0.41f
                else -> 0.42f
            }

            strafe(strafe)
            player.motionY = 0.005
        }
    }

    override fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is C03PacketPlayer && collidesBottom()) {
            packet.y += 0.005
        }
    }

    private fun collidesBottom(): Boolean {
        val world = mc.theWorld ?: return false
        val player = mc.thePlayer ?: return false

        return world.getCollidingBoundingBoxes(player, player.entityBoundingBox.offset(0.0, -0.005, 0.0)).isNotEmpty()
    }

    override fun onJump(event: JumpEvent) {
        event.cancelEvent()
    }
}