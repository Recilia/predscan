/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.flymodes.other

import me.rein.predscan.features.module.modules.movement.Flight.vanillaSpeed
import me.rein.predscan.features.module.modules.movement.flymodes.FlyMode
import me.rein.predscan.utils.movement.MovementUtils.strafe
import me.rein.predscan.utils.client.PacketUtils.sendPackets
import me.rein.predscan.utils.extensions.component1
import me.rein.predscan.utils.extensions.component2
import me.rein.predscan.utils.extensions.component3
import me.rein.predscan.utils.extensions.toRadiansD
import me.rein.predscan.utils.timing.MSTimer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import kotlin.math.cos
import kotlin.math.sin

object MineSecure : FlyMode("MineSecure") {
	private val timer = MSTimer()

	override fun onUpdate() {
		mc.thePlayer.capabilities.isFlying = false

		mc.thePlayer.motionY =
			if (mc.gameSettings.keyBindSneak.isKeyDown) 0.0
			else -0.01

		strafe(vanillaSpeed, true)

		if (!timer.hasTimePassed(150) || !mc.gameSettings.keyBindJump.isKeyDown)
			return

		val (x, y, z) = mc.thePlayer

		sendPackets(
			C04PacketPlayerPosition(x, y + 5, z, false),
			C04PacketPlayerPosition(0.5, -1000.0, 0.5, false)
		)

		val yaw = mc.thePlayer.rotationYaw.toRadiansD()

		mc.thePlayer.setPosition(x - sin(yaw) * 0.4, y, z + cos(yaw) * 0.4)
		timer.reset()
	}
}
