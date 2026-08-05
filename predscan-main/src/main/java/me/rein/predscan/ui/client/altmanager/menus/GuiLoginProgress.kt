/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.ui.client.altmanager.menus

import me.liuli.elixir.account.MinecraftAccount
import me.rein.predscan.handler.lang.translationText
import me.rein.predscan.features.module.modules.client.HUDModule.guiColor
import me.rein.predscan.ui.client.altmanager.GuiAltManager.Companion.login
import me.rein.predscan.ui.font.AWTFontRenderer.Companion.assumeNonVolatile
import me.rein.predscan.utils.render.RenderUtils.drawBloom
import me.rein.predscan.utils.render.RenderUtils.drawLoadingCircle
import me.rein.predscan.utils.ui.AbstractScreen
import net.minecraft.client.gui.GuiScreen
import java.awt.Color

class GuiLoginProgress(
    minecraftAccount: MinecraftAccount,
    success: () -> Unit,
    error: (Exception) -> Unit,
    done: () -> Unit
) : AbstractScreen() {

    init {
        login(minecraftAccount, success, error, done)
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        assumeNonVolatile {
            drawDefaultBackground()
            drawLoadingCircle(width / 2f, height / 4f + 70)
            drawCenteredString(fontRendererObj, translationText(
                "Loggingintoaccount"), width / 2, height / 2 - 60, 16777215)
        }

        drawBloom(mouseX - 5, mouseY - 5, 10, 10, 16, Color(guiColor))

        super.drawScreen(mouseX, mouseY, partialTicks)
    }

}