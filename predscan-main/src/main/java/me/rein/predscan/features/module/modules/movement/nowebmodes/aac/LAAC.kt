/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.nowebmodes.aac

import me.rein.predscan.features.module.modules.movement.nowebmodes.NoWebMode
import me.rein.predscan.utils.extensions.tryJump

object LAAC : NoWebMode("LAAC") {
    override fun onUpdate() {
        if (!mc.thePlayer.isInWeb) {
            return
        }

        mc.thePlayer.jumpMovementFactor = if (mc.thePlayer.movementInput.moveStrafe != 0f) 1f else 1.21f

        if (!mc.gameSettings.keyBindSneak.isKeyDown)
            mc.thePlayer.motionY = 0.0

        if (mc.thePlayer.onGround)
            mc.thePlayer.tryJump()
    }
}
