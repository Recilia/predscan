/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.client

import me.rein.predscan.features.module.Module
import me.rein.predscan.features.module.Category

object TargetModule : Module("Target", Category.CLIENT, Category.SubCategory.CLIENT_GENERAL, gameDetecting = false, canBeEnabled = false) {
    var playerValue by boolean("Player", true)
    var animalValue by boolean("Animal", true)
    var mobValue by boolean("Mob", true)
    var invisibleValue by boolean("Invisible", false)
    var deadValue by boolean("Dead", false)

    override fun handleEvents() = true
}