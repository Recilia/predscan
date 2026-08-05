/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.ui.client.gui

import me.rein.predscan.PredScan
import me.rein.predscan.PredScan.CLIENT_NAME
import me.rein.predscan.features.module.modules.client.HUDModule.guiColor
import me.rein.predscan.handler.api.ClientUpdate
import me.rein.predscan.ui.font.AWTFontRenderer.Companion.assumeNonVolatile
import me.rein.predscan.ui.font.Fonts
import me.rein.predscan.utils.render.RenderUtils.drawBloom
import me.rein.predscan.utils.render.RenderUtils.drawImage
import me.rein.predscan.utils.ui.AbstractScreen
import net.minecraft.client.gui.GuiButton
import net.minecraft.util.ResourceLocation
import java.awt.Color
import java.io.IOException
import kotlin.Float
import kotlin.Int
import kotlin.Throws

class GuiCommitInfo : AbstractScreen() {

    private val gitImage: ResourceLocation = ResourceLocation("${CLIENT_NAME.lowercase()}/texture/mainmenu/github.png")

    override fun initGui() {
        val buttonWidth = 200
        val buttonHeight = 20
        val buttonX = width / 2 - buttonWidth / 2
        val buttonY = height - buttonHeight - 10

        +GuiButton(0, buttonX, buttonY, buttonWidth, buttonHeight, "Back")

        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {

        assumeNonVolatile = true

        drawDefaultBackground()
        drawImage(gitImage, 30, 30, 30, 30)

        val font = Fonts.minecraftFont
        val textColor = Color(255, 255, 255).rgb
        val startX = 70
        val startY = 30

        val lines = listOf(
            "Git Info",
            "$CLIENT_NAME built by ${ClientUpdate.gitInfo.getProperty("git.build.user.name")}",
            "Version: ${ClientUpdate.gitInfo.getProperty("git.build.version")}",
            "CommitId: ${ClientUpdate.gitInfo.getProperty("git.commit.id")} (${ClientUpdate.gitInfo.getProperty("git.commit.id.abbrev")})",
            "CommitMessage: ${ClientUpdate.gitInfo.getProperty("git.commit.message.short")}",
            "Branch: ${ClientUpdate.gitInfo.getProperty("git.branch")}",
            "Remote origin: ${ClientUpdate.gitInfo.getProperty("git.remote.origin.url")}",
            "Developers: ${PredScan.CLIENT_AUTHOR}"
        )

        lines.forEachIndexed { index, line ->
            drawString(font, line, startX, startY + font.FONT_HEIGHT * index + index * 5, textColor)
        }

        drawBloom(mouseX - 5, mouseY - 5, 10, 10, 16, Color(guiColor))

        assumeNonVolatile = false

        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    @Throws(IOException::class)
    override fun actionPerformed(button: GuiButton) {
        if (button.id == 0) {
            mc.displayGuiScreen(null)
        }
    }
}