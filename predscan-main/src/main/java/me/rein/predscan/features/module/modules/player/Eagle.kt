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
import me.rein.predscan.utils.timing.TickTimer
import net.minecraft.client.settings.GameSettings
import net.minecraft.init.Blocks.air
import net.minecraft.util.BlockPos

object Eagle : Module("Eagle", Category.PLAYER, Category.SubCategory.PLAYER_ASSIST) {

    private val maxSneakTime by intRange("MaxSneakTime", 1..5, 0..20)
    private val onlyWhenLookingDown by boolean("OnlyWhenLookingDown", false)
    private val lookDownThreshold by float("LookDownThreshold", 45f, 0f..90f) { onlyWhenLookingDown }

    private val sneakTimer = TickTimer()

    val onUpdate = handler<UpdateEvent> {
        val thePlayer = mc.thePlayer ?: return@handler

        if (GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) return@handler

        if (thePlayer.onGround && BlockPos(thePlayer).down().block == air) {
            val shouldSneak = !onlyWhenLookingDown || thePlayer.rotationPitch >= lookDownThreshold

            mc.gameSettings.keyBindSneak.pressed = shouldSneak && !GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)
        } else {
            if (sneakTimer.hasTimePassed(maxSneakTime.random())) {
                mc.gameSettings.keyBindSneak.pressed = false
                sneakTimer.reset()
            } else sneakTimer.update()
        }
    }

    override fun onDisable() {
        sneakTimer.reset()

        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSneak))
            mc.gameSettings.keyBindSneak.pressed = false
    }
}