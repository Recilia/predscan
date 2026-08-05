/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.nowebmodes.other

import me.rein.predscan.features.module.modules.movement.nowebmodes.NoWebMode
import me.rein.predscan.utils.extensions.tryJump

object Rewi : NoWebMode("Rewi") {
    override fun onUpdate() {
        if (!mc.thePlayer.isInWeb) {
            return
        }
        mc.thePlayer.jumpMovementFactor = 0.42f

        if (mc.thePlayer.onGround)
            mc.thePlayer.tryJump()
    }
}
