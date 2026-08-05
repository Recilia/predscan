/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.player

import me.rein.predscan.event.Render2DEvent
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.Module
import me.rein.predscan.features.module.Category
import me.rein.predscan.file.FileManager.friendsConfig
import me.rein.predscan.file.FileManager.saveConfig
import me.rein.predscan.utils.client.chat
import me.rein.predscan.utils.render.ColorUtils.stripColor
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.input.Mouse

object MidClick : Module("MidClick", Category.PLAYER, Category.SubCategory.PLAYER_ASSIST, subjective = true, gameDetecting = false) {
    private var wasDown = false


    val onRender = handler<Render2DEvent> {
        if (mc.currentScreen != null)
            return@handler

        if (!wasDown && Mouse.isButtonDown(2)) {
            val entity = mc.objectMouseOver.entityHit

            if (entity is EntityPlayer) {
                val playerName = stripColor(entity.name)

                if (!friendsConfig.isFriend(playerName)) {
                    friendsConfig.addFriend(playerName)
                    saveConfig(friendsConfig)
                    chat("§a§l$playerName§c was added to your friends.")
                } else {
                    friendsConfig.removeFriend(playerName)
                    saveConfig(friendsConfig)
                    chat("§a§l$playerName§c was removed from your friends.")
                }

            } else
                chat("§c§lError: §aYou need to select a player.")
        }
        wasDown = Mouse.isButtonDown(2)
    }
}