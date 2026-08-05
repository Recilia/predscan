/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.visual

import me.rein.predscan.event.UpdateEvent
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.ui.client.hud.HUD.addNotification
import me.rein.predscan.ui.client.hud.element.elements.Notification
import me.rein.predscan.ui.client.hud.element.elements.Type
import me.rein.predscan.event.handler

object HealthWarn: Module("HealthWarn", Category.VISUAL, Category.SubCategory.RENDER_OVERLAY, gameDetecting = false) {

    private val healthValue by int("Health", 7, 1.. 20)

    private var canWarn = true

    override fun onEnable() {
        canWarn = true
    }

    override fun onDisable() {
        canWarn = true
    }


    val onUpdate = handler<UpdateEvent> {
        if (mc.thePlayer.health <= healthValue) {
            if (canWarn) {
                addNotification(Notification("HP Warning","YOU ARE AT LOW HP!", Type.ERROR, 3000))

                canWarn = false
            }
        } else {
            canWarn = true
        }
    }
}
