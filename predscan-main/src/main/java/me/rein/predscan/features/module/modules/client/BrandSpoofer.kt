/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.client

import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.features.module.modules.client.button.*
import me.rein.predscan.config.ListValue
import net.minecraft.client.gui.GuiButton
import java.util.*

/**
 * The type Client spoof.
 */
object BrandSpoofer : Module("BrandSpoofer", Category.CLIENT, Category.SubCategory.CLIENT_GENERAL) {
    /**
     * The Mode value.
     */
    val possibleBrands = choices(
        "Mode", arrayOf(
            "Vanilla",
            "OptiFine",
            "Fabric",
            "Feather",
            "LunarClient",
            "LabyMod",
            "CheatBreaker",
            "PvPLounge",
            "Minebuilders",
            "FML",
            "Geyser",
            "Log4j",
            "Custom",
        ), "PredScan"
    )

    val customValue = text("Custom-Brand", "WTF") { possibleBrands.get().equals("Custom", true) }

    private val buttonValue = ListValue(
        "Button",
        arrayOf(
            "Dark",
            "Light",
            "Rounded",
            "LiquidBounce",
            "Fline",
            "PredScan",
            "PVP",
            "Vanilla"
        ),
        "PredScan"
    )

    fun getButtonRenderer(button: GuiButton?): AbstractButtonRenderer? {
        val lowerCaseButtonValue = buttonValue.get().lowercase(Locale.getDefault())
        return when (lowerCaseButtonValue) {
            "rounded" -> RoundedButtonRenderer(button!!)
            "predscan" -> HyperiumButtonRenderer(button!!)
            "pvp" -> PvPClientButtonRenderer(button!!)
            "liquidbounce" -> LiquidButtonRenderer(button!!)
            "light" -> LunarButtonRenderer(button!!)
            "dark" -> BlackoutButtonRenderer(button!!)
            else -> null // vanilla or unknown
        }
    }
}