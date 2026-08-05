/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement

import me.rein.predscan.event.MovementInputEvent
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.utils.extensions.isMoving
import me.rein.predscan.utils.simulation.SimulatedPlayer

object Parkour : Module("Parkour", Category.MOVEMENT, Category.SubCategory.MOVEMENT_EXTRAS, subjective = true, gameDetecting = false) {

    val onMovementInput = handler<MovementInputEvent> { event ->
        val thePlayer = mc.thePlayer ?: return@handler

        val simPlayer = SimulatedPlayer.fromClientPlayer(event.originalInput)

        simPlayer.tick()

        if (thePlayer.isMoving && thePlayer.onGround && !thePlayer.isSneaking && !mc.gameSettings.keyBindSneak.isKeyDown && !simPlayer.onGround) {
            event.originalInput.jump = true
        }

    }
}
