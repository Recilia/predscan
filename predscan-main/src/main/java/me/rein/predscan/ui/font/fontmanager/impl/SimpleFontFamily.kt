/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.ui.font.fontmanager.impl

import me.rein.predscan.ui.font.fontmanager.api.FontFamily
import me.rein.predscan.ui.font.fontmanager.api.FontRenderer
import java.awt.Font

class SimpleFontFamily private constructor(
    override val name: String,
    private val awtFont: Font
) : FontFamily {

    override fun ofSize(size: Int): FontRenderer {
        return SimpleFontRenderer.create(awtFont.deriveFont(Font.PLAIN, size.toFloat()))
    }

    companion object {
        fun create(name: String, awtFont: Font): FontFamily =
            SimpleFontFamily(name, awtFont)
    }
}
