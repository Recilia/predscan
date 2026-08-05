/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.command.commands

import me.rein.predscan.features.command.Command
import me.rein.predscan.ui.client.gui.GuiMainMenu
import net.minecraft.client.gui.GuiMultiplayer
import net.minecraft.client.multiplayer.GuiConnecting
import net.minecraft.client.multiplayer.ServerData

object ConnectCommand : Command("connect") {

    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        when(args.size) {
            3 -> {
                if(args[2] == "silent") {
                    chat("Connecting to §a§l${args[1]} §7(Silent mode)")
                    mc.displayGuiScreen(GuiConnecting(GuiMultiplayer(GuiMainMenu()), mc, ServerData("", args[1], false)))
                }
                return
            }
        }
        if (args.size == 3 && args[2] == "silent") return
        if (args.size == 2) {
            chat("Connecting to §a§l${args[1]}")
            mc.theWorld.sendQuittingDisconnectingPacket()
            mc.displayGuiScreen(GuiConnecting(GuiMultiplayer(GuiMainMenu()), mc, ServerData("", args[1], false)))
        } else chatSyntax("connect <ip:port> (silent)")
    }

}