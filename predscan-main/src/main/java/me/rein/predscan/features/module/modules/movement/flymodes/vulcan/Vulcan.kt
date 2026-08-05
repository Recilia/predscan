/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.flymodes.vulcan

import me.rein.predscan.features.module.modules.movement.flymodes.FlyMode

object Vulcan : FlyMode("Vulcan") {
    override fun onUpdate() {
        if (!mc.thePlayer.onGround && mc.thePlayer.fallDistance > 0) {
            if (mc.thePlayer.ticksExisted % 2 == 0) {
                mc.thePlayer.motionY = -0.155
            } else {
                mc.thePlayer.motionY = -0.1
            }
        }
    }
}
