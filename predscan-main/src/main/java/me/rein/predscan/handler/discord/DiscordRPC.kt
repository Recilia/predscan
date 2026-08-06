/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.handler.discord

import com.jagrosh.discordipc.IPCClient
import com.jagrosh.discordipc.IPCListener
import com.jagrosh.discordipc.entities.RichPresence
import com.jagrosh.discordipc.entities.pipe.PipeStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.rein.predscan.PredScan.CLIENT_VERSION
import me.rein.predscan.event.ClientShutdownEvent
import me.rein.predscan.event.Listenable
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.ModuleManager
import me.rein.predscan.features.module.modules.client.DiscordRPCModule
import me.rein.predscan.utils.client.ClientUtils.LOGGER
import me.rein.predscan.utils.client.MinecraftInstance
import me.rein.predscan.utils.client.ServerUtils
import me.rein.predscan.utils.client.ServerUtils.formatSessionTime
import me.rein.predscan.utils.io.APIConnectorUtils.discordApp
import me.rein.predscan.utils.kotlin.SharedScopes

import org.json.JSONObject
import java.time.OffsetDateTime

object DiscordRPC : MinecraftInstance, Listenable {

    // IPC Client
    private var ipcClient: IPCClient? = null

    private var appID = 0L
    private val assets = mutableMapOf<String, String>()
    private val timestamp = OffsetDateTime.now()

    // Status of running
    var running: Boolean = false

    private var predscanwebsite = "PDF CLIENT for Minecraft"
    private var discordRPCModule: DiscordRPCModule? = null

    /**
     * Setup Discord RPC
     */
    fun run() {
        try {
            running = true

            loadConfiguration()

            ipcClient = IPCClient(appID)
            ipcClient?.setListener(object : IPCListener {

                /**
                 * Fired whenever an [IPCClient] is ready and connected to Discord.
                 *
                 * @param client The now ready IPCClient.
                 */
                override fun onReady(client: IPCClient?) {
                    SharedScopes.IO.launch {
                        while (running) {
                            update()

                            try {
                                delay(1000L)
                            } catch (ignored: InterruptedException) {
                            }
                        }
                    }
                }

                /**
                 * Fired whenever an [IPCClient] has closed.
                 *
                 * @param client The now closed IPCClient.
                 * @param json A [JSONObject] with close data.
                 */
                override fun onClose(client: IPCClient?, json: JSONObject?) {
                    running = false
                }

            })
            ipcClient?.connect()
        } catch (e: Throwable) {
            LOGGER.error("Failed to setup Discord RPC", e)
        }

    }

    /**
     * Update rich presence
     */
    fun update() {
        val builder = RichPresence.Builder()

        // Set playing time
        builder.setStartTimestamp(timestamp)

        // Check assets contains logo and set logo
        if (assets.containsKey("rpc")) {
            // Ensure discordRPCModule is initialized and not null
            discordRPCModule = DiscordRPCModule

            discordRPCModule?.let { module ->
                builder.setLargeImage("image-1024x1024", "made by rein, for predators. <3")

                builder.setDetails("$predscanwebsite$")

                // Set display info based on module settings - options

                val serverInfo = buildString {
                    val serverIP = ServerUtils.remoteIp?.let {
                        if (module.showServerValue) ServerUtils.hideSensitiveInformation(it) else null
                    }

                    if (serverIP != null) append("Server: $serverIP\n")

                    if (module.showNameValue) append("IGN: ${mc.thePlayer?.name ?: mc.session?.username ?: "Unknown"}\n")

                    if (module.showHealthValue) append("HP: ${mc.thePlayer?.health ?: "N/A"}\n")

                    if (module.showModuleValue) {
                        val enabledModules = ModuleManager.count { it.state }
                        append("Enable: $enabledModules of ${ModuleManager.size} Modules\n")
                    }

                    if (module.showOtherValue) {
                        val sessionTime = if (mc.isSingleplayer) "SinglePlayer\n" else formatSessionTime()
                        append("Time: $sessionTime")
                    }
                }

                builder.setState(if (serverInfo.equals("Loading", true)) "Loading" else serverInfo)
            }
        }

        // Check ipc client is connected and send rpc
        if (ipcClient?.status == PipeStatus.CONNECTED)
            ipcClient?.sendRichPresence(builder.build())
    }

    /**
     * Shutdown ipc client
     */
    fun stop() {
        if (ipcClient?.status != PipeStatus.CONNECTED) {
            return
        }

        try {
            ipcClient?.close()
        } catch (e: Throwable) {
            LOGGER.error("Failed to close Discord RPC.", e)
        }
    }

    private val onClientShutdown = handler<ClientShutdownEvent> {
        stop()
    }

    private fun loadConfiguration() {
        appID = discordApp.toLongOrNull() ?: 1040929807510282280L
        assets["rpc"] = "rpc"
    }
}