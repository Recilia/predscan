/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.command.commands

import me.rein.predscan.features.command.Command
import me.rein.predscan.utils.render.ColorUtils
import me.rein.predscan.utils.render.ColorUtils.translateAlternateColorCodes
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.util.EnumChatFormatting
import me.rein.predscan.file.FileManager.friendsConfig
import me.rein.predscan.script.api.global.Chat
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer

object AddAllCommand : Command("addall", arrayOf("").toString()) {
    override fun execute(arguments: Array<String>) {
        if (arguments.size == 2) {
            val tag = translateAlternateColorCodes(arguments[1])
            val count = AtomicInteger(0)
            val friendManager = friendsConfig
            val presistent = arguments[0].contains("")

            mc.thePlayer.sendQueue.playerInfoMap.forEach(
                Consumer { player: NetworkPlayerInfo ->
                    val team = player.playerTeam
                    if (team != null) {
                        if (ColorUtils.stripColor(team.colorPrefix).contains(tag) ||
                            ColorUtils.stripColor(team.colorSuffix).contains(tag)
                        ) {
                            val name = player.gameProfile.name
                            if (!friendManager.isFriend(name)) {
                                friendManager.addFriend(name)
                                count.incrementAndGet()
                                Chat.print("§b[§b!§b]§b ADDED: $name")
                            }
                        }
                    }
                }
            )

            Chat.print("Were added " + EnumChatFormatting.DARK_RED + count.get() + EnumChatFormatting.GRAY + " players.")
        } else {
            chat("§bUse: addall <tag>")
        }
    }
}