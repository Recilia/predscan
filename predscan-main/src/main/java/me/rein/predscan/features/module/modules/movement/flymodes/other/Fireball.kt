/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.flymodes.other

import me.rein.predscan.event.EventState
import me.rein.predscan.event.MotionEvent
import me.rein.predscan.features.module.modules.movement.Flight
import me.rein.predscan.features.module.modules.movement.Flight.autoFireball
import me.rein.predscan.features.module.modules.movement.Flight.options
import me.rein.predscan.features.module.modules.movement.flymodes.FlyMode
import me.rein.predscan.utils.block.center
import me.rein.predscan.utils.client.PacketUtils.sendPacket
import me.rein.predscan.utils.extensions.isMoving
import me.rein.predscan.utils.extensions.isNearEdge
import me.rein.predscan.utils.extensions.sendUseItem
import me.rein.predscan.utils.extensions.tryJump
import me.rein.predscan.utils.inventory.InventoryUtils
import me.rein.predscan.utils.inventory.SilentHotbar
import me.rein.predscan.utils.inventory.hotBarSlot
import me.rein.predscan.utils.rotation.Rotation
import me.rein.predscan.utils.rotation.RotationUtils
import me.rein.predscan.utils.timing.TickedActions.nextTick
import net.minecraft.init.Items
import net.minecraft.network.play.client.C0APacketAnimation
import net.minecraft.util.BlockPos

object Fireball : FlyMode("Fireball") {

    override fun onMotion(event: MotionEvent) {
        val player = mc.thePlayer ?: return

        val fireballSlot = InventoryUtils.findItem(36, 44, Items.fire_charge) ?: return

        if (autoFireball != "Off") {
            SilentHotbar.selectSlotSilently(
                this,
                fireballSlot,
                immediate = true,
                render = autoFireball == "Pick",
                resetManually = true
            )
        }

        if (event.eventState != EventState.POST)
            return

        val customRotation = Rotation(
            if (Flight.invertYaw) RotationUtils.invertYaw(player.rotationYaw) else player.rotationYaw,
            Flight.rotationPitch
        )

        if (player.onGround && !mc.theWorld.isAirBlock(BlockPos(player.posX, player.posY - 1, player.posZ))) {
            Flight.firePosition = BlockPos(player.posX, player.posY - 1, player.posZ)
        }

        val smartRotation = Flight.firePosition?.center?.let { RotationUtils.toRotation(it, false, player) }
        val rotation = if (Flight.pitchMode == "Custom") customRotation else smartRotation

        if (options.rotationsActive && rotation != null) {
            RotationUtils.setTargetRotation(rotation, options, if (options.keepRotation) options.resetTicks else 1)
        }

        if (Flight.fireBallThrowMode == "Edge" && !player.isNearEdge(Flight.edgeThreshold))
            return

        if (Flight.autoJump && player.onGround && !Flight.wasFired) {
            player.tryJump()
        }
    }

    override fun onTick() {
        val player = mc.thePlayer ?: return

        val fireballSlot = InventoryUtils.findItem(36, 44, Items.fire_charge) ?: return

        val fireBall = player.hotBarSlot(fireballSlot).stack

        if (Flight.fireBallThrowMode == "Edge" && !player.isNearEdge(Flight.edgeThreshold))
            return

        if (Flight.wasFired) {
            return
        }

        if (player.isMoving) {
            Flight.nextTick {
                if (Flight.swing) player.swingItem() else sendPacket(C0APacketAnimation())

                // NOTE: You may increase max try to `2` if fireball doesn't work. (Ex: BlocksMC)
                repeat(Flight.fireballTry) {
                    player.sendUseItem(fireBall)
                }

                Flight.nextTick {
                    if (autoFireball != "Off") {
                        SilentHotbar.selectSlotSilently(
                            this,
                            fireballSlot,
                            immediate = true,
                            render = autoFireball == "Pick",
                            resetManually = true
                        )
                    }

                    Flight.wasFired = true
                }
            }
        }
    }

    override fun onDisable() {
        SilentHotbar.resetSlot(this)
    }
}