/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.command.commands

import me.rein.predscan.features.command.Command
import me.rein.predscan.features.module.modules.client.IRCModule

object ChatAdminCommand : Command("chatadmin") {

    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (!IRCModule.state) {
            chat("§cError: §7LiquidChat is disabled!")
            return
        }

        if (args.size <= 1) {
            chatSyntax("chatadmin <ban/unban>")
            return
        }

        when (args[1].lowercase()) {
            "ban" -> {
                if (args.size > 2) {
                    try {
                        IRCModule.chatClient.banUser(args[2])
                    } catch (_: IllegalArgumentException) {
                        chat("§c'${args[2]}' is not a valid username or UUID!")
                    }
                } else {
                    chatSyntax("chatadmin ban <username>")
                }
            }

            "unban" -> {
                if (args.size > 2) {
                    try {
                        IRCModule.chatClient.unbanUser(args[2])
                    } catch (_: IllegalArgumentException) {
                        chat("§c'${args[2]}' is not a valid username or UUID!")
                    }
                } else {
                    chatSyntax("chatadmin unban <username>")
                }
            }
        }
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty())
            return emptyList()

        return when (args.size) {
            1 -> {
                arrayOf("ban", "unban")
                        .map { it.lowercase() }
                        .filter { it.startsWith(args[0], true) }
            }
            else -> emptyList()
        }
    }
}