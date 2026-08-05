/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.speedmodes.vulcan

import me.rein.predscan.features.module.modules.movement.speedmodes.SpeedMode
import me.rein.predscan.utils.extensions.isInLiquid
import me.rein.predscan.utils.movement.MovementUtils.strafe
import me.rein.predscan.utils.extensions.isMoving
import me.rein.predscan.utils.extensions.tryJump

object VulcanHop : SpeedMode("VulcanHop") {
    override fun onUpdate() {
        val player = mc.thePlayer ?: return
        if (player.isInLiquid || player.isInWeb || player.isOnLadder) return

        if (player.isMoving) {
            if (player.isAirBorne && player.fallDistance > 2) {
                mc.timer.timerSpeed = 1f
                return
            }

            if (player.onGround) {
                player.tryJump()
                if (player.motionY > 0) {
                    mc.timer.timerSpeed = 1.1453f
                }
                strafe(0.4815f)
            } else {
                if (player.motionY < 0) {
                    mc.timer.timerSpeed = 0.9185f
                }
            }
        } else {
            mc.timer.timerSpeed = 1f
        }
    }
}