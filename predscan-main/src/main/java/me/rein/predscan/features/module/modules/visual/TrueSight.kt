/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.visual

import me.rein.predscan.event.UpdateEvent
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module

object TrueSight : Module("TrueSight", Category.VISUAL, Category.SubCategory.RENDER_OVERLAY) {
    val barriers by boolean("Barriers", true)
    val entities by boolean("Entities", true)

    val onUpdate = handler<UpdateEvent> {
        if (barriers && mc.gameSettings.particleSetting == 2) {
            mc.gameSettings.particleSetting = 1
        }
    }
}