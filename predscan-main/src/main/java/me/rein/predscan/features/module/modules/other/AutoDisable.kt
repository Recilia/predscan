/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.other

import me.rein.predscan.PredScan.hud
import me.rein.predscan.event.PacketEvent
import me.rein.predscan.event.UpdateEvent
import me.rein.predscan.event.WorldEvent
import me.rein.predscan.features.module.Module
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.modules.combat.KillAura
import me.rein.predscan.features.module.modules.movement.Flight
import me.rein.predscan.features.module.modules.movement.Speed
import me.rein.predscan.features.module.modules.player.scaffolds.*
import me.rein.predscan.utils.client.chat
import me.rein.predscan.ui.client.hud.element.elements.Notification
import me.rein.predscan.ui.client.hud.element.elements.Type
import me.rein.predscan.event.handler
import net.minecraft.network.play.server.S08PacketPlayerPosLook

object AutoDisable : Module("AutoDisable", Category.OTHER, Category.SubCategory.MISCELLANEOUS, gameDetecting = false) {
    private val modulesList = hashSetOf(KillAura, Scaffold, Flight, Speed)

    private val onFlagged by boolean("onFlag", true)
    private val onWorldChange by boolean("onWorldChange", false)
    private val onDeath by boolean("onDeath", false)

    private val warn by choices("Warn", arrayOf("Chat", "Notification"), "Chat")

    val onPacket = handler<PacketEvent> { event ->
        val packet = event.packet

        if (packet is S08PacketPlayerPosLook && onFlagged) {
            disabled("flagged")
        }
    }

    val onUpdate = handler<UpdateEvent> { event ->
        val player = mc.thePlayer ?: return@handler

        if (onDeath && player.isDead) {
            disabled("deaths")
        }
    }

           val onWorld = handler<WorldEvent> { event ->
        if (onWorldChange) {
            disabled("world changed")
        }
    }

    private fun disabled(reason: String) {
        val enabledModules = modulesList.filter { it.state }

        if (enabledModules.isNotEmpty()) {
            enabledModules.forEach { module ->
                module.state = false
            }

            if (warn == "Chat") {
                chat("§eModules have been disabled due to §c$reason")
            } else {
                hud.addNotification(Notification("Modules have been disabled due to $reason", "!!!", Type.INFO, 60))
            }
        }
    }

    fun addModule(module: Module) {
        if (!modulesList.contains(module)) {
            modulesList.add(module)
        }
    }

    fun removeModule(module: Module) {
        modulesList.remove(module)
    }

    fun getModules(): Collection<Module> {
        return modulesList
    }
}