/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.flymodes.verus

import me.rein.predscan.event.JumpEvent
import me.rein.predscan.event.PacketEvent
import me.rein.predscan.features.module.modules.movement.Flight.boostMotion
import me.rein.predscan.features.module.modules.movement.Flight.boostTicksValue
import me.rein.predscan.features.module.modules.movement.Flight.damage
import me.rein.predscan.features.module.modules.movement.Flight.timerSlow
import me.rein.predscan.features.module.modules.movement.Flight.yBoost
import me.rein.predscan.features.module.modules.movement.flymodes.FlyMode
import me.rein.predscan.utils.movement.MovementUtils.strafe
import me.rein.predscan.utils.client.PacketUtils.sendPacket
import me.rein.predscan.utils.extensions.stop
import me.rein.predscan.utils.extensions.stopXZ
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook

/**
 * Modified code ported from VerusDamage Script by Arcane
 *
 * Note:
 * - Getting below block (Like NCPLatest Fly Method), should help to temporarily bypass Speed(A) Checks
 * - Turning off Damage should bypass Fly(G) Checks
 */
object Verus : FlyMode("Verus") {
    private var boostTicks = 0

    override fun onEnable() {
        boostTicks = 0
        if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.entityBoundingBox.offset(0.0, 3.0001, 0.0).expand(0.0, 0.0, 0.0)).isEmpty()) {
            if (damage)
                sendPacket(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false))

            sendPacket(C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false))
            sendPacket(C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true))
        }
        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + yBoost.toDouble(), mc.thePlayer.posZ)
    }

    override fun onDisable() {
        if (boostTicks > 0) {
            mc.thePlayer?.stopXZ()
            mc.timer.timerSpeed = 1f
        }
    }

    override fun onUpdate() {
        mc.thePlayer?.stopXZ()
        mc.thePlayer?.stop()

        if (boostTicks == 0 && mc.thePlayer.hurtTime > 0) {
            boostTicks = boostTicksValue
        }

        boostTicks--

        if (timerSlow) {
            if (mc.thePlayer.ticksExisted % 3 == 0) {
                mc.timer.timerSpeed = 0.15f
            } else {
                mc.timer.timerSpeed = 0.08f
            }
        }

        strafe(boostMotion, true)
    }

    override fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is C03PacketPlayer) {
            packet.onGround = true
        }
    }

    override fun onJump(event: JumpEvent) {
        event.cancelEvent()
    }
}