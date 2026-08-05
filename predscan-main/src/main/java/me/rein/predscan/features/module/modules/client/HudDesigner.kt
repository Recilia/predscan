/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.client

import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.ui.client.hud.designer.GuiHudDesigner

object HudDesigner : Module("HudDesigner", Category.CLIENT, Category.SubCategory.CLIENT_GENERAL, canBeEnabled = false) {
    override fun onEnable() {
        mc.displayGuiScreen(GuiHudDesigner())
    }
}