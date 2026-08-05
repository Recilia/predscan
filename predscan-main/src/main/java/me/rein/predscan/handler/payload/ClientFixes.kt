/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.handler.payload

import io.netty.buffer.Unpooled
import me.rein.predscan.config.Configurable
import me.rein.predscan.event.Listenable
import me.rein.predscan.event.PacketEvent
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.modules.client.BrandSpoofer.customValue
import me.rein.predscan.features.module.modules.client.BrandSpoofer.possibleBrands
import me.rein.predscan.handler.other.AutoReconnect
import me.rein.predscan.utils.client.ClientUtils.LOGGER
import me.rein.predscan.utils.client.MinecraftInstance
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.client.C17PacketCustomPayload

object ClientFixes : Configurable("Features"), MinecraftInstance, Listenable {

    var fmlFixesEnabled by boolean("AntiForge", true)

    var blockFML by boolean("AntiForgeFML", true)

    var blockProxyPacket by boolean("AntiForgeProxy", true)

    var blockPayloadPackets by boolean("AntiForgePayloads", true)

    var blockResourcePackExploit by boolean("FixResourcePackExploit", true)

    val onPacket = handler<PacketEvent> { event ->
        runCatching {
            val packet = event.packet

            if (mc.isIntegratedServerRunning || !fmlFixesEnabled) {
                return@runCatching
            }

            when {
                blockProxyPacket && packet.javaClass.name == "net.minecraftforge.fml.common.network.internal.FMLProxyPacket" -> {
                    event.cancelEvent()
                    return@runCatching
                }

                packet is C17PacketCustomPayload -> when {
                    blockPayloadPackets && !packet.channelName.startsWith("MC|") -> {
                    event.cancelEvent()
                    }
                        packet.channelName == "MC|Brand" -> {
                    packet.data = PacketBuffer(Unpooled.buffer()).writeString(
                        when (possibleBrands.get()) {
                            "Vanilla" -> "vanilla"
                            "LunarClient" -> "lunarclient:v2.20.9-2533"
                            "OptiFine" -> "optifine"
                            "CheatBreaker" -> "CB"
                            "LabyMod" -> "labymod"
                            "Fabric" -> "fabric"
                            "PvPLounge" -> "PLC18"
                            "Geyser" -> "geyser"
                            "Minebuilders" -> "minebuilders9"
                            "Feather" -> "feather"
                            "FML" -> "fml,forge"
                            "Log4j" -> "LOLG4J"
                            "Custom" -> customValue.get()
                            else -> {
                                // do nothing
                                return@runCatching
                            }
                        }
                    )
                }
            }
        }
    }.onFailure {
        LOGGER.error("Failed to handle packet on client fixes.", it)
        }
    }

    var autoReconnectDelayValue = int("AutoReconnectDelay", 5000, AutoReconnect.MIN..AutoReconnect.MAX).onChanged { value ->
        AutoReconnect.isEnabled = value < AutoReconnect.MAX
    }

    @JvmStatic
    fun getClientModName(): String {
        return possibleBrands.get()
    }
}
