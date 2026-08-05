/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.client

import me.rein.predscan.event.EventState
import me.rein.predscan.event.MotionEvent
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.features.module.modules.visual.FreeCam
import me.rein.predscan.utils.rotation.Rotation
import me.rein.predscan.utils.rotation.RotationUtils.currentRotation
import me.rein.predscan.utils.rotation.RotationUtils.serverRotation
import me.rein.predscan.event.handler
import java.awt.Color

object Rotations : Module("Rotations", Category.CLIENT, Category.SubCategory.CLIENT_GENERAL, gameDetecting = false) {

    private val realistic by boolean("Realistic", true)
    private val body by boolean("Body", true) { !realistic }

    private val smoothRotations by boolean("SmoothRotations", false)
    private val smoothingFactor by float("SmoothFactor", 0.15f, 0.1f..0.9f) { smoothRotations }

    val ghost by boolean("Ghost", true)

    val color by color("Color", Color(110, 0, 120)) { ghost }

    val debugRotations by boolean("DebugRotations", false)

    var prevHeadPitch = 0f
    var headPitch = 0f

    private var lastRotation: Rotation? = null

    private val specialCases
        get() = arrayListOf(FreeCam.shouldDisableRotations()).any { it }

    val onMotion = handler<MotionEvent> { event ->
        if (event.eventState != EventState.POST)
            return@handler

        val thePlayer = mc.thePlayer ?: return@handler
        val targetRotation = getRotation() ?: serverRotation

        prevHeadPitch = headPitch
        headPitch = targetRotation.pitch

        thePlayer.rotationYawHead = targetRotation.yaw
        if (shouldRotate() && body && !realistic) {
            thePlayer.renderYawOffset = thePlayer.rotationYawHead
        }

        lastRotation = targetRotation
    }

    fun lerp(tickDelta: Float, old: Float, new: Float): Float {
        return old + (new - old) * tickDelta
    }

    /**
     * Rotate when current rotation is not null.
     */
    fun shouldRotate() = state && (specialCases || currentRotation != null)

    /**
     * Smooth out rotations between two points
     */
    private fun smoothRotation(from: Rotation, to: Rotation): Rotation {
        val diffYaw = to.yaw - from.yaw
        val diffPitch = to.pitch - from.pitch

        val smoothedYaw = from.yaw + diffYaw * smoothingFactor
        val smoothedPitch = from.pitch + diffPitch * smoothingFactor

        return Rotation(smoothedYaw, smoothedPitch)
    }

    /**
     * Imitate the game's head and body rotation logic
     */
    fun shouldUseRealisticMode() = realistic && shouldRotate()

    /**
     * Which rotation should the module use?
     */
    fun getRotation(): Rotation? {
        val currRotation = if (specialCases) serverRotation else currentRotation

        return if (smoothRotations && currRotation != null) {
            smoothRotation(lastRotation ?: return currRotation, currRotation)
        } else {
            currRotation
        }
    }
}