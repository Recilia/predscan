/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.player

import me.rein.predscan.event.MotionEvent
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.utils.movement.MovementUtils.updateControls
import me.rein.predscan.event.handler

object DelayRemover : Module("DelayRemover", Category.PLAYER, Category.SubCategory.PLAYER_COUNTER) {

   // val jumpDelay by boolean("NoJumpDelay", false)
  //  val jumpDelayTicks by IntegerValue("JumpDelayTicks", 0, 0.. 4) { jumpDelay }

    val noClickDelay by boolean("NoClickDelay", true)

    val blockBreakDelay by boolean("NoBlockHitDelay", false)

    val noSlowBreak by boolean("NoSlowBreak", false)
    val air by boolean("Air", true) { noSlowBreak }
    val water by boolean("Water", true) { noSlowBreak }

    val exitGuiValue by boolean("NoExitGuiDelay", true)

    private var prevGui = false


    val onMotion = handler<MotionEvent> {
        if (mc.thePlayer != null && mc.theWorld != null && noClickDelay) {
            mc.leftClickCounter = 0
        }

        if (blockBreakDelay) {
            mc.playerController.blockHitDelay = 0
        }

        if (mc.currentScreen == null && exitGuiValue) {
            if (prevGui) updateControls()
            prevGui = false
        } else {
            prevGui = true
        }
    }

}
