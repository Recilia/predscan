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
import me.rein.predscan.utils.block.block
import net.minecraft.client.settings.GameSettings
import net.minecraft.init.Blocks

object AutoBreak : Module("AutoBreak", Category.PLAYER, Category.SubCategory.PLAYER_ASSIST, subjective = true, gameDetecting = false) {

    val onUpdate = handler<UpdateEvent> {
        mc.theWorld ?: return@handler

        val target = mc.objectMouseOver?.blockPos ?: return@handler

        mc.gameSettings.keyBindAttack.pressed = target.block != Blocks.air
    }

    override fun onDisable() {
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindAttack))
            mc.gameSettings.keyBindAttack.pressed = false
    }
}