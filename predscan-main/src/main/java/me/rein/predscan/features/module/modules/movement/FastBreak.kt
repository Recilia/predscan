/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement

import me.rein.predscan.event.UpdateEvent
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.features.module.modules.other.Fucker
import me.rein.predscan.features.module.modules.other.Nuker

object FastBreak : Module("FastBreak", Category.MOVEMENT, Category.SubCategory.MOVEMENT_EXTRAS) {

    private val breakDamage by float("BreakDamage", 0.8F, 0.1F..1F)

    val onUpdate = handler<UpdateEvent> {
        mc.playerController.blockHitDelay = 0

        if (mc.playerController.curBlockDamageMP > breakDamage)
            mc.playerController.curBlockDamageMP = 1F

        if (Fucker.currentDamage > breakDamage)
            Fucker.currentDamage = 1F

        if (Nuker.currentDamage > breakDamage)
            Nuker.currentDamage = 1F
    }
}