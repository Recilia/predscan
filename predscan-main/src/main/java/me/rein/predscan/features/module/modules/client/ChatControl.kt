/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.client

import me.rein.predscan.features.module.Module
import me.rein.predscan.features.module.Category

object ChatControl : Module("ChatControl", Category.CLIENT, Category.SubCategory.CLIENT_GENERAL, gameDetecting = false, subjective = true) {

    init {
        state = true
    }

    val chatLimitValue by boolean("NoChatLimit", true)
    val chatClearValue by boolean("NoChatClear", true)
    private val fontChat by boolean("FontChat", false)

    fun shouldModifyChatFont() = handleEvents() && fontChat
}