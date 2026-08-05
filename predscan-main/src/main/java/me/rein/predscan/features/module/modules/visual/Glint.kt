/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.visual

import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.utils.render.ColorUtils
import java.awt.Color

object Glint: Module("Glint", Category.VISUAL, Category.SubCategory.RENDER_SELF, gameDetecting = false) {

    private val modeValue by choices("Mode", arrayOf("Rainbow", "Custom"), "Custom")
    private val color by color("Color", Color.WHITE) { modeValue == "Custom" }

    fun getGlintColor(): Color {
        return when (modeValue.lowercase()) {
            "rainbow" -> ColorUtils.rainbow(10, 0.9F)
            else -> Color(color.rgb)
        }
    }
}