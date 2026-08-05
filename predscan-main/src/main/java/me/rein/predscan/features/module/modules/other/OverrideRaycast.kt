/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.other

import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module

object OverrideRaycast : Module("OverrideRaycast", Category.OTHER, Category.SubCategory.MISCELLANEOUS, gameDetecting = false) {
    private val alwaysActive by boolean("AlwaysActive", true)

    fun shouldOverride() = handleEvents() || alwaysActive
}