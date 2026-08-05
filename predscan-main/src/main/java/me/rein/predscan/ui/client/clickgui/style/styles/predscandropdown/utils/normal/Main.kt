/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.ui.client.clickgui.style.styles.predscandropdown.utils.normal

import me.rein.predscan.PredScan.moduleManager
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module

object Main {
    var categoryCount: Int = 0

    var reloadModules: Boolean = false

    @JvmField
    var allowedClickGuiHeight: Float = 300f

    fun getModulesInCategory(category: Category): List<Module> {
        return moduleManager.filter { module -> module.category == category }
    }

}