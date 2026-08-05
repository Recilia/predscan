/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.player

import me.rein.predscan.event.MotionEvent
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.utils.client.PacketUtils.sendPacket
import me.rein.predscan.utils.inventory.SilentHotbar
import me.rein.predscan.utils.inventory.InventoryUtils
import me.rein.predscan.event.handler
import net.minecraft.init.Items
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement

object KeepAlive : Module("KeepAlive", Category.PLAYER, Category.SubCategory.PLAYER_ASSIST) {

    val mode by choices("Mode", arrayOf("/heal", "Soup"), "/heal")

    private var runOnce = false


    val onMotion = handler<MotionEvent> {
        val thePlayer = mc.thePlayer ?: return@handler

        if (thePlayer.isDead || thePlayer.health <= 0) {
            if (runOnce) return@handler

            when (mode.lowercase()) {
                "/heal" -> thePlayer.sendChatMessage("/heal")
                "soup" -> {
                    val soupInHotbar = InventoryUtils.findItem(36, 44, Items.mushroom_stew)

                    if (soupInHotbar != null) {
                        SilentHotbar.selectSlotSilently(this,
                            soupInHotbar,
                            immediate = true,
                            render = false,
                            resetManually = true
                        )
                        sendPacket(C08PacketPlayerBlockPlacement(thePlayer.heldItem))
                        SilentHotbar.resetSlot(this)
                    }
                }
            }

            runOnce = true
        } else
            runOnce = false
    }
}