/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * Repository: https://github.com/SkidderMC/PredScan/
 */

package me.rein.predscan.ui.client.clickgui.style.styles.predscandropdown.utils.normal

/**
 * Interface representing a basic screen structure in the client GUI.
 * It defines methods for handling initialization, input events, and rendering.
 */
interface Screen {
    /**
     * Initializes the GUI components and prepares the screen for rendering.
     *
     */
    fun initGui()

    /**
     * Handles key input events.
     *
     */
    fun keyTyped(typedChar: Char, keyCode: Int)

    /**
     * Renders the screen and its components.
     *
     */
    fun drawScreen(mouseX: Int, mouseY: Int)

    /**
     * Handles mouse click events.
     *
     */
    fun mouseClicked(mouseX: Int, mouseY: Int, button: Int)

    /**
     * Handles mouse release events.
     *
     */
    fun mouseReleased(mouseX: Int, mouseY: Int, state: Int)
}
