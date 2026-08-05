/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.ui.client.clickgui.style.styles.yzygui.panel.element.impl

import me.rein.predscan.PredScan
import me.rein.predscan.ui.client.clickgui.style.styles.yzygui.category.yzyCategory
import me.rein.predscan.ui.client.clickgui.style.styles.yzygui.panel.Panel
import me.rein.predscan.ui.client.clickgui.style.styles.yzygui.panel.element.PanelElement
import me.rein.predscan.utils.render.RenderUtils
import me.rein.predscan.config.BoolValue
import java.awt.Color

/**
 * Boolean Element - YZY GUI
 * @author opZywl
 */
class BooleanElement(
    private val element: ModuleElement,
    private val setting: BoolValue,
    parent: Panel,
    x: Int,
    y: Int,
    width: Int,
    height: Int
) : PanelElement(parent, x, y, width, height) {

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val color = if (setting.get()) {
            yzyCategory.of(element.module.category)?.color ?: Color(26, 26, 26)
        } else {
            Color(26, 26, 26)
        }

        RenderUtils.yzyRectangle(
            x.toFloat(), y.toFloat(),
            width.toFloat(), height.toFloat(),
            color
        )

        PredScan.customFontManager["lato-bold-15"]?.drawString(
            setting.name,
            (x + 1).toFloat(),
            y + (height / 4.0f) + 0.5f,
            -1
        )
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        if (isHovering(mouseX, mouseY)) {
            setting.toggle()
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {}

    override fun keyTyped(character: Char, code: Int) {}
}
