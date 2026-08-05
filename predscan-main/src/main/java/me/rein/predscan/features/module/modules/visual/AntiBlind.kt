/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.visual

import me.rein.predscan.event.PacketEvent
import me.rein.predscan.features.module.Module
import me.rein.predscan.features.module.Category
import me.rein.predscan.event.handler
import net.minecraft.network.play.server.S3FPacketCustomPayload

object AntiBlind : Module("AntiBlind", Category.VISUAL, Category.SubCategory.RENDER_SELF, gameDetecting = false) {
    val confusionEffect by boolean("Confusion", true)
    val pumpkinEffect by boolean("Pumpkin", true)
    val fireEffect by float("FireAlpha", 0.3f, 0f..1f)
    val bossHealth by boolean("BossHealth", true)
    private val bookPage by boolean("BookPage", true)
    val achievements by boolean("Achievements", true)

    val onPacket = handler<PacketEvent> { event ->
        if (!bookPage) return@handler
        val packet = event.packet
        if (packet is S3FPacketCustomPayload && packet.channelName == "MC|BOpen") event.cancelEvent()
    }
}