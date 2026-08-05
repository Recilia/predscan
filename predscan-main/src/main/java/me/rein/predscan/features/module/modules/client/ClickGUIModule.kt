/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.client

import me.rein.predscan.PredScan.clickGui
import me.rein.predscan.event.PacketEvent
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.ui.client.clickgui.ClickGui
import me.rein.predscan.ui.client.clickgui.style.styles.BlackStyle
import me.rein.predscan.ui.client.clickgui.style.styles.predscandropdown.PredScanDropdownClickGUI
import me.rein.predscan.ui.client.clickgui.style.styles.nlclickgui.NeverloseGui
import me.rein.predscan.ui.client.clickgui.style.styles.yzygui.YzYGui
import me.rein.predscan.utils.client.ClientThemesUtils
import me.rein.predscan.utils.render.ColorUtils.fade
import net.minecraft.network.play.server.S2EPacketCloseWindow
import org.lwjgl.input.Keyboard
import java.awt.Color

object ClickGUIModule : Module("ClickGUI", Category.CLIENT, Category.SubCategory.CLIENT_GENERAL,  Keyboard.KEY_RSHIFT, canBeEnabled = false) {
    var lastScale = 0

    private var predscanDropdownGui: PredScanDropdownClickGUI? = null
    private var yzyGui: YzYGui? = null
    private var neverloseGui: NeverloseGui? = null

    private val style by choices(
        "Style",
        arrayOf("Black", "Zywl", "Dropdown", "PredScan"),
        "PredScan"
    ).onChanged {
        updateStyle()
        resetGuiInstances()
    }

    private val color by choices(
        "Color", arrayOf("Custom", "Fade", "Theme"), "Theme"
    ) { style == "Dropdown" }

    private val customColorSetting by color("CustomColor", Color(255, 255, 255)) { color == "Custom" || color == "Fade" }

    var scale by float("Scale", 1.0f, 0.5f..1.5f)
    val maxElements by int("MaxElements", 15, 1..30)
    val fadeSpeed by float("FadeSpeed", 1f, 0.5f..4f)
    val scrolls by boolean("Scrolls", true)
    val spacedModules by boolean("SpacedModules", false)
    val panelsForcedInBoundaries by boolean("PanelsForcedInBoundaries", false)

    val headerColor by boolean("Header Color", true) { style == "Dropdown" }

    val categoryOutline by boolean("Outline", true) { style == "Dropdown" }

    val roundedRectRadius by float("RoundedRect-Radius", 0F, 0F..2F)  { style == "Dropdown" }

    val backback by boolean("Background Accent", true) { style == "Dropdown" }
    val scrollMode by choices("Scroll Mode", arrayOf("Screen Height", "Value"), "Value")  { style == "Dropdown" }
    val colormode by choices("Setting Accent", arrayOf("White", "Color"), "Color") { style == "Dropdown" }
    val clickHeight by int("Tab Height", 250, 100.. 500) { style == "Dropdown" }

    override fun onEnable() {
        try {
            Keyboard.enableRepeatEvents(true)
            lastScale = mc.gameSettings.guiScale
            mc.gameSettings.guiScale = 2

            when {
                style.equals("Zywl", ignoreCase = true) -> {
                    if (yzyGui == null) {
                        yzyGui = YzYGui(this)
                    } else {
                        yzyGui?.resetGui()
                    }
                    mc.displayGuiScreen(yzyGui)
                    this.state = false
                }
                style.equals("Dropdown", ignoreCase = true) -> {
                    if (predscanDropdownGui == null) {
                        predscanDropdownGui = PredScanDropdownClickGUI()
                    } else {
                        predscanDropdownGui?.resetGui()
                    }
                    mc.displayGuiScreen(predscanDropdownGui)
                    this.state = false
                }
                style.equals("PredScan", ignoreCase = true) -> {
                    if (neverloseGui == null) {
                        neverloseGui = NeverloseGui()
                    }
                    mc.displayGuiScreen(neverloseGui)
                    this.state = false
                }
                else -> {
                    updateStyle()
                    mc.displayGuiScreen(clickGui)
                    this.state = false
                }
            }
        } catch (e: Exception) {
            println("Error opening ClickGUI: ${e.message}")
            updateStyle()
            mc.displayGuiScreen(clickGui)
            this.state = false
        }
    }

    override fun onDisable() {
        Keyboard.enableRepeatEvents(false)
    }

    private fun resetGuiInstances() {
        try {
            predscanDropdownGui?.onGuiClosed()
            yzyGui?.onGuiClosed()
            neverloseGui = null
        } catch (e: Exception) {
            println("Error during GUI cleanup: ${e.message}")
        }
        predscanDropdownGui = null
        yzyGui = null
    }

    private fun updateStyle() {
        clickGui.style = when (style) {
            "Black" -> BlackStyle
            else -> return
        }
    }

    fun generateColor(index: Int): Color {
        return when (color) {
            "Custom" -> {
                customColorSetting
            }
            "Fade" -> {
                fade(customColorSetting, index * 10, 100)
            }
            "Theme" -> {
                ClientThemesUtils.getColor(index)
            }
            else -> ClientThemesUtils.getColor(index)
        }
    }

    val onPacket = handler<PacketEvent>(always = true) { event ->
        if (event.packet is S2EPacketCloseWindow && mc.currentScreen is ClickGui) {
            event.cancelEvent()
        }
    }
}