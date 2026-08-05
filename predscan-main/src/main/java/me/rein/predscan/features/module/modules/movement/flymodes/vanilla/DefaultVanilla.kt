/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.flymodes.vanilla


import me.rein.predscan.event.MoveEvent
import me.rein.predscan.event.PacketEvent
import me.rein.predscan.features.module.modules.movement.Flight.kickBypassModeValue
import me.rein.predscan.features.module.modules.movement.Flight.kickBypassMotionSpeedValue
import me.rein.predscan.features.module.modules.movement.Flight.kickBypassValue
import me.rein.predscan.features.module.modules.movement.Flight.noClipValue
import me.rein.predscan.features.module.modules.movement.Flight.smoothValue
import me.rein.predscan.features.module.modules.movement.Flight.speedValue
import me.rein.predscan.features.module.modules.movement.Flight.spoofValue
import me.rein.predscan.features.module.modules.movement.Flight.vspeedValue
import me.rein.predscan.features.module.modules.movement.flymodes.FlyMode
import me.rein.predscan.utils.movement.MovementUtils
import me.rein.predscan.utils.movement.MovementUtils.resetMotion
import me.rein.predscan.utils.client.PacketUtils.sendPacket
import me.rein.predscan.utils.timing.MSTimer
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.util.AxisAlignedBB

object DefaultVanilla : FlyMode("DefaultVanilla") {

    private var packets = 0
    private var kickBypassMotion = 0f
    private val groundTimer = MSTimer()
    val player: EntityPlayerSP
        get() = mc.thePlayer

    override fun onEnable() {
        packets = 0
        kickBypassMotion = 0f
    }

    override fun onMove(event: MoveEvent) {
        if (mc.thePlayer.isDead) return

        if (smoothValue) {
            player.capabilities.isFlying = true
            player.capabilities.flySpeed = speedValue * 0.05f
        } else {
            player.capabilities.isFlying = false
            resetMotion(true)
            if (mc.gameSettings.keyBindJump.isKeyDown) player.motionY += vspeedValue
            if (mc.gameSettings.keyBindSneak.isKeyDown) player.motionY -= vspeedValue
            MovementUtils.strafe(speedValue)
        }

        if (noClipValue) {
            player.noClip = true
        }

        if (kickBypassValue && kickBypassModeValue == "Motion") {
            kickBypassMotion = kickBypassMotionSpeedValue

            if (player.ticksExisted % 2 == 0) {
                kickBypassMotion = -kickBypassMotion
            }

            if (!mc.gameSettings.keyBindJump.pressed && !mc.gameSettings.keyBindSneak.pressed) {
                player.motionY = kickBypassMotion.toDouble()
            }
        }

        var ySpeed = 0.0
        if (mc.gameSettings.keyBindJump.isKeyDown) ySpeed += vspeedValue
        if (mc.gameSettings.keyBindSneak.isKeyDown) ySpeed -= vspeedValue

        player.motionY = ySpeed
        event.y = ySpeed

        player.onGround = false
        player.isInWeb = false
        
        if (kickBypassValue && kickBypassModeValue == "Packet" && packets++ >= 40) {
            packets = 0
            handleVanillaKickBypass()
        }
    }


    override fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is C03PacketPlayer) {
            if (spoofValue) packet.onGround = true
            if (packets++ >= 40 && kickBypassValue && kickBypassModeValue == "Packet") {
                packets = 0
                handleVanillaKickBypass()
            }
        }
    }

    private fun handleVanillaKickBypass() {
        if (!kickBypassValue || !groundTimer.hasTimePassed(1000)) return
        val ground = calculateGround() + 0.5
        var posY = mc.thePlayer.posY

        while (posY > ground) {
            sendPacket(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, posY, mc.thePlayer.posZ, true))
            if (posY - 8.0 < ground) break
            posY -= 8.0
        }

        sendPacket(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, ground, mc.thePlayer.posZ, true))
        posY = ground

        while (posY < mc.thePlayer.posY) {
            sendPacket(C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, posY, mc.thePlayer.posZ, true))
            if (posY + 8.0 > mc.thePlayer.posY) break
            posY += 8.0
        }

        sendPacket(
            C03PacketPlayer.C04PacketPlayerPosition(
                mc.thePlayer.posX,
                mc.thePlayer.posY,
                mc.thePlayer.posZ,
                true
            )
        )
        groundTimer.reset()
    }

    private fun calculateGround(): Double {
        val playerBoundingBox = mc.thePlayer.entityBoundingBox
        var blockHeight = 0.05
        var ground = mc.thePlayer.posY

        while (ground > 0.0) {
            val customBox = AxisAlignedBB.fromBounds(
                playerBoundingBox.maxX,
                ground + blockHeight,
                playerBoundingBox.maxZ,
                playerBoundingBox.minX,
                ground,
                playerBoundingBox.minZ
            )

            if (mc.theWorld.checkBlockCollision(customBox)) {
                if (blockHeight <= 0.05) return ground + blockHeight
                ground += blockHeight
                blockHeight = 0.05
            }

            ground -= blockHeight
        }
        return 0.0
    }
}
