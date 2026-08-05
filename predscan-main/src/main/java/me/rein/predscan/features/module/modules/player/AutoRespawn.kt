/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.player

import me.rein.predscan.event.UpdateEvent
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.features.module.modules.exploit.Ghost
import net.minecraft.client.gui.GuiGameOver

object AutoRespawn : Module("AutoRespawn", Category.PLAYER, Category.SubCategory.PLAYER_ASSIST, gameDetecting = false) {

    private val instant by boolean("Instant", true)

    val onUpdate = handler<UpdateEvent> {
        val thePlayer = mc.thePlayer

        if (thePlayer == null || Ghost.handleEvents())
            return@handler

        if (if (instant) mc.thePlayer.health == 0F || mc.thePlayer.isDead else mc.currentScreen is GuiGameOver && (mc.currentScreen as GuiGameOver).enableButtonsTimer >= 20) {
            thePlayer.respawnPlayer()
            mc.displayGuiScreen(null)
        }
    }
}