/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.client.button

import me.rein.predscan.features.module.modules.client.HUDModule
import me.rein.predscan.features.module.modules.client.HUDModule.buttonShadowValue
import me.rein.predscan.features.module.modules.client.HUDModule.uiEffectValue
import me.rein.predscan.utils.render.shader.UIEffectRenderer.drawShadowWithCustomAlpha
import me.rein.predscan.utils.render.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import java.awt.Color
import kotlin.math.sqrt

class RoundedButtonRenderer(button: GuiButton) : AbstractButtonRenderer(button) {

    val hud = HUDModule

    override fun render(mouseX: Int, mouseY: Int, mc: Minecraft) {
        RenderUtils.drawRoundedCornerRect(button.xPosition.toFloat(), button.yPosition.toFloat(),
            button.xPosition + button.width.toFloat(), button.yPosition + button.height.toFloat(),
            sqrt((button.width * button.height).toDouble()).toFloat() * 0.1f,
            (if (button.hovered) {
                Color(60, 60, 60, 150)
            } else {
                Color(31, 31, 31, 150)
            }).rgb)
        if (hud.handleEvents() && uiEffectValue && buttonShadowValue) {
            drawShadowWithCustomAlpha(button.xPosition.toFloat(),
                button.yPosition.toFloat(),
                button.width.toFloat(),
                button.height.toFloat(),
                240f)
        }
    }
}
