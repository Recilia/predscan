/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.ui.client.gui

import me.rein.predscan.features.module.modules.client.HUDModule.guiColor
import me.rein.predscan.ui.font.AWTFontRenderer.Companion.assumeNonVolatile
import me.rein.predscan.utils.io.APIConnectorUtils
import me.rein.predscan.utils.io.APIConnectorUtils.donate
import me.rein.predscan.utils.io.URLRegistryUtils
import me.rein.predscan.utils.io.MiscUtils
import me.rein.predscan.utils.render.RenderUtils.drawBloom
import me.rein.predscan.utils.ui.AbstractScreen
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Keyboard
import java.awt.Color

class GuiInfo(private val prevGui: GuiScreen) : AbstractScreen() {

    override fun initGui() {
        val yOffset = height / 4 + 20
        val buttonWidth = 200
        val buttonHeight = 20

        val buttons = listOf(
            +GuiButton(1, width / 2 - buttonWidth / 2, yOffset + buttonHeight * 0, "Open Website"),
            +GuiButton(2, width / 2 - buttonWidth / 2, yOffset + buttonHeight * 1 + 10, "Join Discord Server"),
            +GuiButton(3, width / 2 - buttonWidth / 2, yOffset + buttonHeight * 2 + 20, "Server Status"),
            +GuiButton(4, width / 2 - buttonWidth / 2, yOffset + buttonHeight * 3 + 30, "Scripts"),
            +GuiButton(5, width / 2 - buttonWidth / 2, yOffset + buttonHeight * 4 + 40, "Client Configuration"),
            +GuiButton(6, width / 2 - buttonWidth / 2, yOffset + buttonHeight * 5 + 50, "Donate Now"),
            +GuiButton(7, width / 2 - buttonWidth / 2, yOffset + buttonHeight * 6 + 60, "Done")
        )

        buttonList.addAll(buttons)

        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {

        assumeNonVolatile = true

        drawDefaultBackground()

        drawBloom(mouseX - 5, mouseY - 5, 10, 10, 16, Color(guiColor))

        assumeNonVolatile = false

        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(prevGui)
        }
        super.keyTyped(typedChar, keyCode)
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            1 -> MiscUtils.showURL(URLRegistryUtils.WEBSITE)
            2 -> MiscUtils.showURL(APIConnectorUtils.discord)
            3 -> mc.displayGuiScreen(GuiServerStatus(this))
            4 -> mc.displayGuiScreen(GuiScripts(this))
            5 -> mc.displayGuiScreen(GuiClientConfiguration(this))
            6 -> MiscUtils.showURL(donate)
            7 -> mc.displayGuiScreen(prevGui)
        }
    }
}
