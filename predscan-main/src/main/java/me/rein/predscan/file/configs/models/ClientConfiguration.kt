/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.file.configs.models

import me.rein.predscan.PredScan
import me.rein.predscan.config.Configurable
import me.rein.predscan.utils.client.MinecraftInstance
import me.rein.predscan.utils.client.MinecraftInstance.Companion
import me.rein.predscan.utils.render.IconUtils
import org.lwjgl.opengl.Display

object ClientConfiguration : Configurable("ClientConfiguration"), MinecraftInstance {
    var clientTitle by boolean("ClientTitle", true)
    var customBackground by boolean("CustomBackground", true)
    var particles by boolean("Particles", true)
    var stylisedAlts by boolean("StylisedAlts", true)
    var unformattedAlts by boolean("CleanAlts", true)
    var altsLength by int("AltsLength", 16, 4..20)
    var altsPrefix by text("AltsPrefix", "")
    // The game language can be overridden by the user. empty=default
    var overrideLanguage by text("OverrideLanguage","")

    fun updateClientWindow() {
        if (clientTitle) {
            // Set PredScan title
            Display.setTitle(PredScan.clientTitle)
            // Update favicon
            IconUtils.getFavicon()?.let { icons ->
                Display.setIcon(icons)
            }
        } else {
            // Set original title
            Display.setTitle("Minecraft 1.8.9")
            // Update favicon
            mc.setWindowIcon()
        }
    }
}