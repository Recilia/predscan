/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.flymodes.vanilla

import me.rein.predscan.event.MoveEvent
import me.rein.predscan.features.module.modules.movement.Flight.handleVanillaKickBypass
import me.rein.predscan.features.module.modules.movement.Flight.vanillaSpeed
import me.rein.predscan.features.module.modules.movement.flymodes.FlyMode
import me.rein.predscan.utils.movement.MovementUtils.strafe

object Vanilla : FlyMode("Vanilla") {
	override fun onMove(event: MoveEvent) {
        val thePlayer = mc.thePlayer ?: return

		strafe(vanillaSpeed, true, event)

        thePlayer.onGround = false
        thePlayer.isInWeb = false

        thePlayer.capabilities.isFlying = false

        var ySpeed = 0.0

        if (mc.gameSettings.keyBindJump.isKeyDown)
            ySpeed += vanillaSpeed

        if (mc.gameSettings.keyBindSneak.isKeyDown)
            ySpeed -= vanillaSpeed

        thePlayer.motionY = ySpeed
        event.y = ySpeed

		handleVanillaKickBypass()
	}
}
