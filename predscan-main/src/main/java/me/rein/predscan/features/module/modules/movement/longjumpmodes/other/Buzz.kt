/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.longjumpmodes.other

import me.rein.predscan.features.module.modules.movement.longjumpmodes.LongJumpMode
import me.rein.predscan.utils.movement.MovementUtils

object Buzz : LongJumpMode("Buzz") {
    override fun onUpdate() {
        mc.thePlayer.motionY += 0.4679942989799998
        MovementUtils.speed *= 0.7578698f
    }
}
