/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.longjumpmodes

import me.rein.predscan.event.JumpEvent
import me.rein.predscan.event.MoveEvent
import me.rein.predscan.utils.client.MinecraftInstance

open class LongJumpMode(val modeName: String) : MinecraftInstance {
    open fun onUpdate() {}
    open fun onMove(event: MoveEvent) {}
    open fun onJump(event: JumpEvent) {}

    open fun onEnable() {}
    open fun onDisable() {}
}