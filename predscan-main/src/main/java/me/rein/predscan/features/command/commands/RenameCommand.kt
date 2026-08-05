/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.command.commands

import me.rein.predscan.features.command.Command
import me.rein.predscan.utils.client.PacketUtils.sendPacket
import me.rein.predscan.utils.kotlin.StringUtils
import me.rein.predscan.utils.render.ColorUtils
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction

object RenameCommand : Command("rename") {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size > 1) {
            if (mc.playerController.isNotCreative) {
                chat("§c§lError: §3You need to be in creative mode.")
                return
            }

            val item = mc.thePlayer.heldItem

            if (item?.item == null) {
                chat("§c§lError: §3You need to hold a item.")
                return
            }

            item.setStackDisplayName(ColorUtils.translateAlternateColorCodes(StringUtils.toCompleteString(args, 1)))
            sendPacket(C10PacketCreativeInventoryAction(36 + mc.thePlayer.inventory.currentItem, item))
            chat("§3Item renamed to '${item.displayName}§3'")
            return
        }

        chatSyntax("rename <name>")
    }
}