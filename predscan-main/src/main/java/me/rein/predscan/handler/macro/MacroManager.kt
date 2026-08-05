/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.handler.macro

import me.rein.predscan.event.KeyEvent
import me.rein.predscan.event.Listenable
import me.rein.predscan.event.handler
import me.rein.predscan.utils.client.MinecraftInstance

object MacroManager : MinecraftInstance, Listenable {
    val macros = ArrayList<Macro>()

    val onKey = handler<KeyEvent> { event ->
        macros.filter { it.key == event.key }.forEach { it.exec() }
    }

    override fun handleEvents() = true
}