/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.flymodes.vulcan

import me.rein.predscan.event.BlockBBEvent
import me.rein.predscan.event.PacketEvent
import me.rein.predscan.features.module.modules.movement.flymodes.FlyMode
import me.rein.predscan.utils.client.chat
import me.rein.predscan.utils.client.pos
import me.rein.predscan.utils.extensions.offset
import net.minecraft.block.BlockLadder
import net.minecraft.block.material.Material
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.util.AxisAlignedBB

object VulcanGhost : FlyMode("VulcanGhost") {

    override fun onEnable() {
        chat("Ensure that you sneak on landing.")
        chat("After landing, go backward (Air) and go forward to landing location, then sneak again.")
        chat("And then you can turn off fly.")
    }

    override fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is S08PacketPlayerPosLook) {
            event.cancelEvent()
        }
    }

    override fun onBB(event: BlockBBEvent) {
        if (!mc.gameSettings.keyBindJump.isKeyDown && mc.gameSettings.keyBindSneak.isKeyDown) return
        if (!event.block.material.blocksMovement() && event.block.material != Material.carpet && event.block.material != Material.vine && event.block.material != Material.snow && event.block !is BlockLadder) {
            event.boundingBox = AxisAlignedBB(-2.0, -1.0, -2.0, 2.0, 1.0, 2.0).offset(event.pos)
        }
    }
}
