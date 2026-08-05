/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.client

import me.rein.predscan.event.Render3DEvent
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.utils.render.RenderWings
import java.awt.Color

object Wings : Module("Wings", Category.CLIENT, Category.SubCategory.CLIENT_GENERAL) {
    private val onlyThirdPerson by boolean("OnlyThirdPerson", true)
    val colorType by choices("Color Type", arrayOf("Custom", "Theme", "None"), "Custom")

    val color by color("Color", Color(0xFF0054)) { colorType == "Custom" }

    val wingStyle by choices("Wing Style", arrayOf("Dragon", "Simple"), "Dragon")

    val onRender3D = handler<Render3DEvent> { event ->
        if (onlyThirdPerson && mc.gameSettings.thirdPersonView == 0) return@handler
        val renderWings = RenderWings()
        renderWings.renderWings(event.partialTicks)
    }
}