/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.speedmodes.spectre

import me.rein.predscan.features.module.modules.movement.speedmodes.SpeedMode
import me.rein.predscan.utils.movement.MovementUtils.strafe
import me.rein.predscan.utils.extensions.isMoving

object SpectreLowHop : SpeedMode("SpectreLowHop") {
    override fun onMotion() {
        if (!mc.thePlayer.isMoving || mc.thePlayer.movementInput.jump) return
        if (mc.thePlayer.onGround) {
            strafe(1.1f)
            mc.thePlayer.motionY = 0.15
            return
        }
        strafe()
    }

}