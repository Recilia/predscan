/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.ui.client.clickgui

import kotlinx.coroutines.*
import me.rein.predscan.PredScan.CLIENT_NAME
import me.rein.predscan.PredScan.moduleManager
import me.rein.predscan.config.SettingsUtils
import me.rein.predscan.handler.api.ClientApi
import me.rein.predscan.handler.api.autoSettingsList
import me.rein.predscan.handler.api.loadSettings
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.modules.client.ClickGUIModule
import me.rein.predscan.features.module.modules.client.ClickGUIModule.scale
import me.rein.predscan.features.module.modules.client.ClickGUIModule.scrolls
import me.rein.predscan.features.module.modules.client.HUDModule.guiColor
import me.rein.predscan.file.FileManager.clickGuiConfig
import me.rein.predscan.file.FileManager.saveConfig
import me.rein.predscan.ui.client.clickgui.elements.ButtonElement
import me.rein.predscan.ui.client.clickgui.elements.ModuleElement
import me.rein.predscan.ui.client.clickgui.style.Style
import me.rein.predscan.ui.client.clickgui.style.styles.BlackStyle
import me.rein.predscan.ui.client.hud.HUD
import me.rein.predscan.ui.client.hud.designer.GuiHudDesigner
import me.rein.predscan.ui.client.hud.element.elements.Notification
import me.rein.predscan.ui.client.hud.element.elements.Type
import me.rein.predscan.ui.font.AWTFontRenderer.Companion.assumeNonVolatile
import me.rein.predscan.utils.client.ClientUtils
import me.rein.predscan.utils.client.asResourceLocation
import me.rein.predscan.utils.client.chat
import me.rein.predscan.utils.client.playSound
import me.rein.predscan.utils.kotlin.SharedScopes
import me.rein.predscan.utils.render.RenderUtils.deltaTime
import me.rein.predscan.utils.render.RenderUtils.drawBloom
import me.rein.predscan.utils.render.RenderUtils.drawImage
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager.disableLighting
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11.glScaled
import java.awt.Color
import kotlin.math.roundToInt

object ClickGui : GuiScreen() {

    // Note: hash key = [Panel.name]
    val panels = linkedSetOf<Panel>()
    private val hudIcon = ResourceLocation("${CLIENT_NAME.lowercase()}/custom_hud_icon.png")
    var style: Style = BlackStyle
    private var mouseX = 0
        set(value) {
            field = value.coerceAtLeast(0)
        }
    private var mouseY = 0
        set(value) {
            field = value.coerceAtLeast(0)
        }

    private var autoScrollY: Int? = null

    // Used when closing ClickGui using its key bind, prevents it from getting closed instantly after getting opened.
    // Caused by keyTyped being called along with onKey that opens the ClickGui.
    private var ignoreClosing = false

    fun setDefault() {
        panels.clear()

        val width = 100
        val height = 18
        var yPos = 5

        for (category in Category.entries) {
            panels += Panel(
                category.displayName,
                x = 100,
                y = yPos,
                width,
                height,
                false,
                moduleManager[category].map(::ModuleElement)
            )

            yPos += 20
        }

        yPos += 20

        // Settings Panel
        yPos += 20
        panels += setupSettingsPanel(100, yPos, width, height)
    }

    private fun setupSettingsPanel(xPos: Int = 100, yPos: Int, width: Int, height: Int): Panel {
        val list = autoSettingsList?.map { setting ->
            ButtonElement(setting.name, { Integer.MAX_VALUE }) {
                SharedScopes.IO.launch {
                    try {
                        chat("Loading settings...")

                        // Load settings and apply them
                        val settings = ClientApi.getSettingsScript(settingId = setting.settingId)

                        chat("Applying settings...")
                        SettingsUtils.applyScript(settings)

                        chat("§6Settings applied successfully.")
                        HUD.addNotification(Notification("Updated Settings", "!!!", Type.INFO, 60))
                        mc.playSound("random.anvil_use".asResourceLocation())
                    } catch (e: Exception) {
                        ClientUtils.LOGGER.error("Failed to load settings", e)
                        chat("Failed to load settings: ${e.message}")
                    }
                }
            }.apply {
                this.hoverText = buildString {
                    appendLine("§7Description: §e${setting.description.ifBlank { "No description available" }}")
                    appendLine("§7Type: §e${setting.type.displayName}")
                    appendLine("§7Contributors: §e${setting.contributors}")
                    appendLine("§7Last updated: §e${setting.date}")
                    append("§7Status: §e${setting.statusType.displayName} §a(${setting.statusDate})")
                }
            }
        } ?: run {
            // Try load settings
            loadSettings(useCached = true) {
                mc.addScheduledTask {
                    setupSettingsPanel(xPos, yPos, width, height)
                }
            }

            emptyList()
        }

        return Panel("Auto Settings", xPos, yPos, width, height, false, list)
    }

    override fun drawScreen(x: Int, y: Int, partialTicks: Float) {
        // Enable DisplayList optimization
        assumeNonVolatile {
            mouseX = (x / scale).roundToInt()
            mouseY = (y / scale).roundToInt()

            drawDefaultBackground()
            drawImage(hudIcon, 9, height - 41, 32, 32)

            val scale = scale.toDouble()
            glScaled(scale, scale, scale)

            for (panel in panels) {
                panel.updateFade(deltaTime)
                panel.drawScreenAndClick(mouseX, mouseY)
            }

            descriptions@ for (panel in panels.reversed()) {
                // Don't draw hover text when hovering over a panel header.
                if (panel.isHovered(mouseX, mouseY)) break

                for (element in panel.elements) {
                    if (element is ButtonElement) {
                        if (element.isVisible && element.hoverText.isNotBlank() && element.isHovered(
                                mouseX, mouseY
                            ) && element.y <= panel.y + panel.fade
                        ) {
                            style.drawHoverText(mouseX, mouseY, element.hoverText)
                            // Don't draw hover text for any elements below.
                            break@descriptions
                        }
                    }
                }
            }

            if (Mouse.hasWheel()) {
                val wheel = autoScrollY?.let { it - y } ?: Mouse.getDWheel()
                if (wheel != 0) {
                    var handledScroll = false

                    // Handle foremost panel.
                    for (panel in panels.reversed()) {
                        if (panel.handleScroll(mouseX, mouseY, wheel)) {
                            handledScroll = true
                            break
                        }
                    }

                    if (!handledScroll) handleScroll(wheel)
                }
            }

            disableLighting()
            RenderHelper.disableStandardItemLighting()
            glScaled(1.0, 1.0, 1.0)
        }

        drawBloom(mouseX - 5, mouseY - 5, 10, 10, 16, Color(guiColor))

        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    private fun handleScroll(wheel: Int) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            scale += wheel * 0.0001f

            for (panel in panels) {
                panel.x = panel.parseX()
                panel.y = panel.parseY()
            }

        } else if (scrolls) {
            for (panel in panels) panel.y = panel.parseY(panel.y + wheel / 10)
        }
    }

    public override fun mouseClicked(x: Int, y: Int, mouseButton: Int) {
        if (mouseButton == 0 && x in 5..50 && y in height - 50..height - 5) {
            mc.displayGuiScreen(GuiHudDesigner())
            return
        }

        if (mouseButton == 2) {
            autoScrollY = y
        }

        mouseX = (x / scale).roundToInt()
        mouseY = (y / scale).roundToInt()

        // Handle foremost panel.
        panels.reversed().forEachIndexed { index, panel ->
            if (panel.mouseClicked(mouseX, mouseY, mouseButton)) return

            panel.drag = false

            if (mouseButton == 0 && panel.isHovered(mouseX, mouseY)) {
                panel.x2 = panel.x - mouseX
                panel.y2 = panel.y - mouseY
                panel.drag = true

                // Move dragged panel to top.
                panels.remove(panel)
                panels += panel
                return
            }
        }
    }

    public override fun mouseReleased(x: Int, y: Int, button: Int) {
        mouseX = (x / scale).roundToInt()
        mouseY = (y / scale).roundToInt()

        if (button == 2) {
            autoScrollY = null
        }

        for (panel in panels) panel.mouseReleased(mouseX, mouseY, button)
    }

    override fun updateScreen() {
        if (style is BlackStyle) {
            for (panel in panels) {
                for (element in panel.elements) {
                    if (element is ButtonElement) element.hoverTime += if (element.isHovered(mouseX, mouseY)) 1 else -1

                    if (element is ModuleElement) element.slowlyFade += if (element.module.state) 20 else -20
                }
            }
        }

        super.updateScreen()
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        // Close ClickGUI by using its key bind.
        if (keyCode in arrayOf(ClickGUIModule.keyBind, Keyboard.KEY_ESCAPE)) {
            if (style.chosenText != null) {
                style.chosenText = null
                return
            }

            if (keyCode != Keyboard.KEY_ESCAPE) {
                if (ignoreClosing) {
                    ignoreClosing = false
                } else {
                    mc.displayGuiScreen(null)
                }

                return
            }
        }

        style.chosenText?.processInput(typedChar, keyCode) { style.moveRGBAIndexBy(it) }

        super.keyTyped(typedChar, keyCode)
    }

    override fun onGuiClosed() {
        autoScrollY = null
        saveConfig(clickGuiConfig)
        Keyboard.enableRepeatEvents(false)
        for (panel in panels) panel.fade = 0
    }

    override fun initGui() {
        ignoreClosing = true
    }

    fun Int.clamp(min: Int, max: Int): Int = this.coerceIn(min, max.coerceAtLeast(0))

    override fun doesGuiPauseGame() = false

}