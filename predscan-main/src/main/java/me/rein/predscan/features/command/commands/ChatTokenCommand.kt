/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.command.commands

import me.rein.predscan.PredScan.commandManager
import me.rein.predscan.features.command.Command
import me.rein.predscan.features.module.modules.client.IRCModule
import me.rein.predscan.utils.kotlin.StringUtils
import me.rein.predscan.utils.io.MiscUtils

object ChatTokenCommand : Command("chattoken") {

    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size <= 1) {
            chatSyntax("chattoken <set/copy/generate>")
            return
        }

        when (args[1].lowercase()) {
            "set" -> {
                if (args.size > 2) {
                    IRCModule.jwtToken = StringUtils.toCompleteString(args, 2)
                    IRCModule.jwt = true
                    IRCModule.reloadIfEnabled()
                } else {
                    chatSyntax("chattoken set <token>")
                }
            }

            "generate" -> {
                if (!IRCModule.state) {
                    chat("§cError: §7LiquidChat is disabled!")
                    return
                }

                IRCModule.chatClient.requestJWT()
            }

            "copy" -> {
                if (IRCModule.jwtToken.isEmpty()) {
                    chat("§cError: §7No token set! Generate one first using '${commandManager.prefix}chattoken generate'.")
                    return
                }

                MiscUtils.copy(IRCModule.jwtToken)
                chat("§aCopied to clipboard! Length = ${IRCModule.jwtToken.length}")
            }
        }
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty())
            return emptyList()

        return when (args.size) {
            1 -> {
                arrayOf("set", "generate", "copy")
                        .map { it.lowercase() }
                        .filter { it.startsWith(args[0], true) }
            }
            else -> emptyList()
        }
    }

}