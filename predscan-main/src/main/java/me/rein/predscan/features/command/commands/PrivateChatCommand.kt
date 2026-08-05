/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.command.commands

import me.rein.predscan.features.command.Command
import me.rein.predscan.features.module.modules.client.IRCModule
import me.rein.predscan.utils.kotlin.StringUtils

object PrivateChatCommand : Command("pchat", "privatechat", "lcpm") {

    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size > 2) {
            if (!IRCModule.state) {
                chat("§cError: §7IRC is disabled!")
                return
            }

            val target = args[1]
            val message = StringUtils.toCompleteString(args, 2)

            if (!IRCModule.chatClient.privateMessage(target, message)) {
                chat("§cError: §7IRC is currently not connected to the server!")
                return
            }

            chat("Message was successfully sent.")
        } else
            chatSyntax("pchat <username> <message>")
    }
}