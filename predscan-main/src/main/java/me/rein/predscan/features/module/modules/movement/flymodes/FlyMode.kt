/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.flymodes

import me.rein.predscan.event.*
import me.rein.predscan.utils.client.MinecraftInstance

open class FlyMode(val modeName: String) : MinecraftInstance {
	open fun onMove(event: MoveEvent) {}
	open fun onPacket(event: PacketEvent) {}
	open fun onRender3D(event: Render3DEvent) {}
	open fun onBB(event: BlockBBEvent) {}
	open fun onJump(event: JumpEvent) {}
	open fun onStep(event: StepEvent) {}
	open fun onMotion(event: MotionEvent) {}
	open fun onUpdate() {}

	open fun onEnable() {}
	open fun onDisable() {}
	open fun onTick () {}
}