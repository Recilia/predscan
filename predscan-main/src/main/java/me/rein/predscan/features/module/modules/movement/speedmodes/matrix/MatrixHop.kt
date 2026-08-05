/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.speedmodes.matrix

import me.rein.predscan.features.module.modules.movement.Speed
import me.rein.predscan.features.module.modules.movement.speedmodes.SpeedMode
import me.rein.predscan.features.module.modules.player.scaffolds.Scaffold
import me.rein.predscan.utils.extensions.isInLiquid
import me.rein.predscan.utils.movement.MovementUtils.speed
import me.rein.predscan.utils.movement.MovementUtils.strafe
import me.rein.predscan.utils.extensions.isMoving

object MatrixHop : SpeedMode("MatrixHop") {

    override fun onUpdate()  {
        val player = mc.thePlayer ?: return
        if (player.isInLiquid || player.isInWeb || player.isOnLadder) return

        if (Speed.matrixLowHop) player.jumpMovementFactor = 0.026f

        if (player.isMoving) {
            if (player.onGround) {
                strafe(if (!Scaffold.handleEvents()) speed + Speed.extraGroundBoost else speed)
                player.motionY = 0.42 - if (Speed.matrixLowHop) 3.48E-3 else 0.0
            } else {
                if (!Scaffold.handleEvents() && speed < 0.19) {
                    strafe()
                }
            }

            if (player.fallDistance <= 0.4 && player.moveStrafing == 0f) {
                player.speedInAir = 0.02035f
            } else {
                player.speedInAir = 0.02f
            }
        }
    }
}