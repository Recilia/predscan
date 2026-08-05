/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement

import me.rein.predscan.event.JumpEvent
import me.rein.predscan.event.StrafeEvent
import me.rein.predscan.event.UpdateEvent
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.utils.extensions.isInLiquid
import me.rein.predscan.utils.extensions.isMoving
import me.rein.predscan.utils.extensions.toDegreesF
import me.rein.predscan.utils.extensions.tryJump
import me.rein.predscan.utils.movement.MovementUtils.direction
import me.rein.predscan.utils.movement.MovementUtils.speed
import kotlin.math.cos
import kotlin.math.sin

object Strafe : Module("Strafe", Category.MOVEMENT, Category.SubCategory.MOVEMENT_MAIN, gameDetecting = false) {

    private val strength by float("Strength", 0.5F, 0F..1F)
    private val noMoveStop by boolean("NoMoveStop", false)
    private val onGroundStrafe by boolean("OnGroundStrafe", false)
    private val allDirectionsJump by boolean("AllDirectionsJump", false)

    private var wasDown = false
    private var jump = false

    val onJump = handler<JumpEvent> { event ->
        if (jump) {
            event.cancelEvent()
        }
    }

    override fun onEnable() {
        wasDown = false
    }

    val onUpdate = handler<UpdateEvent> {
        if (mc.thePlayer.onGround && mc.gameSettings.keyBindJump.isKeyDown && allDirectionsJump && mc.thePlayer.isMoving && !(mc.thePlayer.isInLiquid || mc.thePlayer.isOnLadder || mc.thePlayer.isInWeb)) {
            if (mc.gameSettings.keyBindJump.isKeyDown) {
                mc.gameSettings.keyBindJump.pressed = false
                wasDown = true
            }
            val yaw = mc.thePlayer.rotationYaw
            mc.thePlayer.rotationYaw = direction.toDegreesF()
            mc.thePlayer.tryJump()
            mc.thePlayer.rotationYaw = yaw
            jump = true
            if (wasDown) {
                mc.gameSettings.keyBindJump.pressed = true
                wasDown = false
            }
        } else {
            jump = false
        }
    }

    val onStrafe = handler<StrafeEvent> {
        if (!mc.thePlayer.isMoving) {
            if (noMoveStop) {
                mc.thePlayer.motionX = .0
                mc.thePlayer.motionZ = .0
            }
            return@handler
        }

        val shotSpeed = speed
        val speed = shotSpeed * strength
        val motionX = mc.thePlayer.motionX * (1 - strength)
        val motionZ = mc.thePlayer.motionZ * (1 - strength)

        if (!mc.thePlayer.onGround || onGroundStrafe) {
            val yaw = direction
            mc.thePlayer.motionX = -sin(yaw) * speed + motionX
            mc.thePlayer.motionZ = cos(yaw) * speed + motionZ
        }
    }
}
