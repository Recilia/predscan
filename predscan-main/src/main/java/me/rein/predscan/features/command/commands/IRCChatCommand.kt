/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.command.commands

import me.rein.predscan.features.command.Command
import me.rein.predscan.features.module.modules.client.IRCModule
import me.rein.predscan.utils.kotlin.StringUtils

object IRCChatCommand : Command("chat", "lc", "irc") {

    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size > 1) {
            if (!IRCModule.state) {
                chat("§cError: §7IRC is disabled!")
                return
            }

            val message = StringUtils.toCompleteString(args, 1)

            if (!IRCModule.chatClient.message(message)) {
                chat("§cError: §bIRC is currently not connected to the server!")
                return
            }
        } else
            chatSyntax("chat <message>")
    }
}