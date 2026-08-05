/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.speedmodes.other

import me.rein.predscan.event.MoveEvent
import me.rein.predscan.features.module.modules.movement.Speed
import me.rein.predscan.features.module.modules.movement.speedmodes.SpeedMode
import me.rein.predscan.utils.movement.MovementUtils.direction
import me.rein.predscan.utils.extensions.isMoving
import me.rein.predscan.utils.timing.MSTimer
import kotlin.math.cos
import kotlin.math.sin

object TeleportCubeCraft : SpeedMode("TeleportCubeCraft") {
    private val timer = MSTimer()
    override fun onMove(event: MoveEvent) {
        if (mc.thePlayer.isMoving && mc.thePlayer.onGround && timer.hasTimePassed(300)) {
            val yaw = direction
            val length = Speed.cubecraftPortLength
            event.x = -sin(yaw) * length
            event.z = cos(yaw) * length
            timer.reset()
        }
    }
}