/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.flymodes.aac

import me.rein.predscan.features.module.modules.movement.Flight.aacFast
import me.rein.predscan.features.module.modules.movement.flymodes.FlyMode

object AAC305 : FlyMode("AAC3.0.5") {
    private var tick = 0

    override fun onUpdate() {
        if (tick == 2)
            mc.thePlayer.motionY = 0.1
        else if (tick > 2) tick = 0

        if (aacFast) {
            if (mc.thePlayer.movementInput.moveStrafe == 0f) mc.thePlayer.jumpMovementFactor = 0.08f
            else mc.thePlayer.jumpMovementFactor = 0f
        }

        tick++
    }
}
