/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.flymodes.spartan

import me.rein.predscan.features.module.modules.movement.flymodes.FlyMode
import me.rein.predscan.utils.movement.MovementUtils.strafe
import me.rein.predscan.utils.client.PacketUtils.sendPacket
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

object Spartan2 : FlyMode("Spartan2") {
	override fun onUpdate() {
		strafe(0.264f)

		if (mc.thePlayer.ticksExisted % 8 == 0)
			sendPacket(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 10, mc.thePlayer.posZ, true))
	}
}
