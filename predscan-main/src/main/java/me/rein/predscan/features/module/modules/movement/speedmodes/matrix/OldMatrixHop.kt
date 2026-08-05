/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.speedmodes.matrix

import me.rein.predscan.features.module.modules.movement.speedmodes.SpeedMode
import me.rein.predscan.utils.extensions.isInLiquid
import me.rein.predscan.utils.movement.MovementUtils.strafe
import me.rein.predscan.utils.extensions.isMoving
import me.rein.predscan.utils.extensions.tryJump

object OldMatrixHop : SpeedMode("OldMatrixHop") {
    
    override fun onUpdate() {
        val player = mc.thePlayer ?: return
        if (player.isInLiquid || player.isInWeb || player.isOnLadder) return

        if (player.isMoving) {
            if (player.onGround) {
                player.tryJump()
                player.speedInAir = 0.02098f
                mc.timer.timerSpeed = 1.055f
            } else {
                strafe()
            }    
        } else {
            mc.timer.timerSpeed = 1f    
        }
    }
}
