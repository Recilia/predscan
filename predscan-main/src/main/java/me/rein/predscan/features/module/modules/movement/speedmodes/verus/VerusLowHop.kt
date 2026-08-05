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

object VerusLowHop : SpeedMode("VerusLowHop") {

    private var speed = 0.0f

    override fun onUpdate() {
        val player = mc.thePlayer ?: return
        if (player.isInLiquid || player.isInWeb || player.isOnLadder) return

        if (player.isMoving) {
            if (player.onGround) {
                speed = if (player.isPotionActive(Potion.moveSpeed)
                    && player.getActivePotionEffect(Potion.moveSpeed).amplifier >= 1)
                    0.5f else 0.36f

                player.tryJump()
            } else {
                if (player.airTicks <= 1) {
                    player.motionY = -0.09800000190734863
                }

                speed *= 0.98f
            }

            strafe(speed)
        }
    }
}