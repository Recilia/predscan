/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.flymodes.other

import me.rein.predscan.features.module.modules.movement.Flight.startY
import me.rein.predscan.features.module.modules.movement.flymodes.FlyMode
import me.rein.predscan.utils.movement.MovementUtils.strafe
import me.rein.predscan.utils.extensions.stopXZ
import me.rein.predscan.utils.kotlin.RandomUtils.nextDouble

object WatchCat : FlyMode("WatchCat") {
	override fun onUpdate() {
		strafe(0.15f)
		mc.thePlayer.isSprinting = true

		if (mc.thePlayer.posY < startY + 2) {
			mc.thePlayer.motionY = nextDouble(endInclusive = 0.5)
			return
		}

		if (startY > mc.thePlayer.posY) mc.thePlayer.stopXZ()
	}
}
