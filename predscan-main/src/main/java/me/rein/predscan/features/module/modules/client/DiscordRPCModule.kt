/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.client

import me.rein.predscan.PredScan.discordRPC
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
object DiscordRPCModule : Module("DiscordRPC", Category.CLIENT, Category.SubCategory.CLIENT_GENERAL) {

    val showServerValue by boolean("ShowServer", false)
    val showNameValue by boolean("ShowName", true)
    val showHealthValue by boolean("ShowHealth", false)
    val showOtherValue by boolean("ShowOther", false)
    val showModuleValue by boolean("ShowModule", false)
    val animated by boolean("ShouldAnimate", true)

    override fun onEnable() {
        discordRPC.run()
    }

    override fun onDisable() {
        discordRPC.stop()
    }
}
