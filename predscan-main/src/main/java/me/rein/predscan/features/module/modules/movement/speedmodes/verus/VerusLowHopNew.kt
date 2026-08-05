/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.speedmodes.verus

import me.rein.predscan.features.module.modules.movement.speedmodes.SpeedMode
import me.rein.predscan.utils.extensions.airTicks
import me.rein.predscan.utils.extensions.isInLiquid
import me.rein.predscan.utils.movement.MovementUtils.airTicks
import me.rein.predscan.utils.movement.MovementUtils.strafe
import me.rein.predscan.utils.extensions.isMoving
import me.rein.predscan.utils.extensions.tryJump
import net.minecraft.potion.Potion

object VerusLowHopNew : SpeedMode("VerusLowHopNew") {

    private var speed = 0.0f

    override fun onUpdate() {
        val player = mc.thePlayer ?: return
        if (player.isInLiquid || player.isInWeb || player.isOnLadder) return

        if (player.isMoving) {
            if (player.onGround) {
                player.tryJump()

                // Checks the presence of Speed potion effect 1 & 2+
                if (player.isPotionActive(Potion.moveSpeed)) {
                    val amplifier = player.getActivePotionEffect(Potion.moveSpeed).amplifier

                    speed = when {
                        amplifier == 1 -> 0.55f
                        amplifier >= 2 -> 0.7f
                        else -> 0.33f
                    }
                }

                // Checks the presence of Slowness potion effect.
                speed = if (player.isPotionActive(Potion.moveSlowdown)
                    && player.getActivePotionEffect(Potion.moveSlowdown).amplifier == 1) {
                    0.3f
                } else {
                    0.33f
                }
            } else {
                if (player.airTicks <= 1) {
                    mc.thePlayer.motionY = -0.09800000190734863
                }

                speed *= 0.99f
            }

            strafe(speed, false)
        }
    }
}