/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.visual

import me.rein.predscan.event.PacketEvent
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.Module
import me.rein.predscan.features.module.Category
import net.minecraft.network.play.server.S3FPacketCustomPayload

object NoBooks : Module("NoBooks", Category.VISUAL, Category.SubCategory.RENDER_SELF, gameDetecting = false) {

    val onPacket = handler<PacketEvent> { event ->
        val packet = event.packet
        if (packet is S3FPacketCustomPayload && packet.channelName == "MC|BOpen") event.cancelEvent()
    }
}