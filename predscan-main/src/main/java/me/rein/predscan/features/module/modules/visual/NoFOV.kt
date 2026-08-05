/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.visual

import me.rein.predscan.features.module.Module
import me.rein.predscan.features.module.Category

object NoFOV : Module("NoFOV", Category.VISUAL, Category.SubCategory.RENDER_SELF, gameDetecting = false) {
    val fov by float("FOV", 1f, 0f..1.5f)
}
