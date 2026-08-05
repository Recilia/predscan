/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.speedmodes.other

import me.rein.predscan.features.module.modules.movement.speedmodes.SpeedMode
import me.rein.predscan.utils.extensions.isMoving
import me.rein.predscan.utils.extensions.tryJump

object Legit : SpeedMode("Legit") {
    override fun onStrafe() {
        val player = mc.thePlayer ?: return

        if (mc.thePlayer.onGround && player.isMoving) {
            player.tryJump()
        }
    }

    override fun onUpdate() {
        val player = mc.thePlayer ?: return

        player.isSprinting = player.movementInput.moveForward > 0.8
    }
}
