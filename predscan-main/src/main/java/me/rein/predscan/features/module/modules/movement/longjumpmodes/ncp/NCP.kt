/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.longjumpmodes.ncp

import me.rein.predscan.event.MoveEvent
import me.rein.predscan.features.module.modules.movement.LongJump.canBoost
import me.rein.predscan.features.module.modules.movement.LongJump.jumped
import me.rein.predscan.features.module.modules.movement.LongJump.ncpBoost
import me.rein.predscan.features.module.modules.movement.longjumpmodes.LongJumpMode
import me.rein.predscan.utils.movement.MovementUtils.speed
import me.rein.predscan.utils.extensions.isMoving

object NCP : LongJumpMode("NCP") {
    override fun onUpdate() {
        speed *= if (canBoost) ncpBoost else 1f
        canBoost = false
    }

    override fun onMove(event: MoveEvent) {
        if (!mc.thePlayer.isMoving && jumped) {
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
            event.zeroXZ()
        }
    }
}