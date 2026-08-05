/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.speedmodes.spectre

import me.rein.predscan.event.MoveEvent
import me.rein.predscan.features.module.modules.movement.speedmodes.SpeedMode
import me.rein.predscan.utils.extensions.isMoving
import me.rein.predscan.utils.extensions.toRadians
import kotlin.math.cos
import kotlin.math.sin

object SpectreOnGround : SpeedMode("SpectreOnGround") {
    private var speedUp = 0
    override fun onMove(event: MoveEvent) {
        if (!mc.thePlayer.isMoving || mc.thePlayer.movementInput.jump) return
        if (speedUp >= 10) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.motionX = 0.0
                mc.thePlayer.motionZ = 0.0
                speedUp = 0
            }
            return
        }
        if (mc.thePlayer.onGround && mc.gameSettings.keyBindForward.isKeyDown) {
            val f = mc.thePlayer.rotationYaw.toRadians()
            mc.thePlayer.motionX -= sin(f) * 0.145f
            mc.thePlayer.motionZ += cos(f) * 0.145f
            event.x = mc.thePlayer.motionX
            event.y = 0.005
            event.z = mc.thePlayer.motionZ
            speedUp++
        }
    }
}