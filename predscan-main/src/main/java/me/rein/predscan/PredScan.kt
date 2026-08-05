/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan

import com.formdev.flatlaf.themes.FlatMacLightLaf
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import me.rein.predscan.event.ClientShutdownEvent
import me.rein.predscan.event.EventManager
import me.rein.predscan.event.StartupEvent
import me.rein.predscan.features.command.CommandManager
import me.rein.predscan.features.command.CommandManager.registerCommands
import me.rein.predscan.features.module.ModuleManager
import me.rein.predscan.features.module.ModuleManager.registerModules
import me.rein.predscan.file.FileManager
import me.rein.predscan.file.FileManager.loadAllConfigs
import me.rein.predscan.file.FileManager.saveAllConfigs
import me.rein.predscan.file.configs.models.ClientConfiguration.updateClientWindow
import me.rein.predscan.handler.api.ClientUpdate
import me.rein.predscan.handler.api.ClientUpdate.gitInfo
import me.rein.predscan.handler.api.loadSettings
import me.rein.predscan.handler.api.messageOfTheDay
import me.rein.predscan.handler.api.reloadMessageOfTheDay
import me.rein.predscan.handler.cape.CapeService
import me.rein.predscan.handler.combat.CombatManager
import me.rein.predscan.handler.discord.DiscordRPC
import me.rein.predscan.handler.lang.LanguageManager.loadLanguages
import me.rein.predscan.handler.macro.MacroManager
import me.rein.predscan.handler.payload.ClientFixes
import me.rein.predscan.handler.spotify.SpotifyIntegration
import me.rein.predscan.handler.tabs.BlocksTab
import me.rein.predscan.handler.tabs.ExploitsTab
import me.rein.predscan.handler.tabs.HeadsTab
import me.rein.predscan.script.ScriptManager
import me.rein.predscan.script.ScriptManager.enableScripts
import me.rein.predscan.script.ScriptManager.loadScripts
import me.rein.predscan.script.remapper.Remapper
import me.rein.predscan.script.remapper.Remapper.loadSrg
import me.rein.predscan.ui.client.altmanager.GuiAltManager.Companion.loadActiveGenerators
import me.rein.predscan.ui.client.clickgui.ClickGui
import me.rein.predscan.ui.client.clickgui.style.styles.yzygui.font.manager.FontManager
import me.rein.predscan.ui.client.clickgui.style.styles.yzygui.manager.GUIManager
import me.rein.predscan.ui.client.hud.HUD
import me.rein.predscan.ui.client.keybind.KeyBindManager
import me.rein.predscan.ui.font.Fonts
import me.rein.predscan.utils.client.ClassUtils.hasForge
import me.rein.predscan.utils.client.ClientUtils.LOGGER
import me.rein.predscan.utils.client.ClientUtils.disableFastRender
import me.rein.predscan.utils.client.BlinkUtils
import me.rein.predscan.utils.client.PacketUtils
import me.rein.predscan.utils.inventory.InventoryManager
import me.rein.predscan.utils.io.MiscUtils
import me.rein.predscan.utils.io.MiscUtils.showErrorPopup
import me.rein.predscan.utils.kotlin.SharedScopes
import me.rein.predscan.utils.inventory.InventoryUtils
import me.rein.predscan.utils.inventory.SilentHotbar
import me.rein.predscan.utils.io.APIConnectorUtils.performAllChecksAsync
import me.rein.predscan.utils.movement.BPSUtils
import me.rein.predscan.utils.movement.MovementUtils
import me.rein.predscan.utils.movement.TimerBalanceUtils
import me.rein.predscan.utils.render.MiniMapRegister
import me.rein.predscan.utils.render.shader.Background
import me.rein.predscan.utils.rotation.RotationUtils
import me.rein.predscan.utils.timing.TickedActions
import me.rein.predscan.utils.timing.WaitTickUtils
import javax.swing.UIManager
import java.util.concurrent.CompletableFuture

object PredScan {

    /**
     * Client Information
     *
     * This has all of the basic information.
     */
    const val CLIENT_NAME = "predscan"
    const val CLIENT_AUTHOR = "rein"
    const val CLIENT_CLOUD = "https://github.com/Recilia/predscan"
    const val CLIENT_WEBSITE = "https://www.reddit.com/r/Overwatch/comments/1rnumog/what_does_predscan_mean/"
    const val CLIENT_GITHUB = "https://github.com/Recilia/predscan"
    const val CLIENT_VERSION = "a1"

    val clientVersionText = gitInfo["git.build.version"]?.toString() ?: "unknown"
    val clientVersionNumber = clientVersionText.substring(1).toIntOrNull() ?: 0 // version format: "b<VERSION>" on legacy
    val clientCommit = gitInfo["git.commit.id.abbrev"]?.let { "git-$it" } ?: "unknown"
    val clientBranch = gitInfo["git.branch"]?.toString() ?: "unknown"

    /**
     * Defines if the client is in development mode.
     * This will enable update checking on commit time instead of regular legacy versioning.
     */
    const val IN_DEV = true

    val clientTitle = buildString(32) {
        append(CLIENT_NAME)
        append(' ')
        append(clientVersionText)
        append(' ')
        append(clientCommit)
        if (IN_DEV) {
            append(" | DEVELOPMENT BUILD")
        }
    }

    var isStarting = true
    var isLoadingConfig = true

    // Managers
    val moduleManager = ModuleManager
    val commandManager = CommandManager
    val eventManager = EventManager
    val fileManager = FileManager
    val scriptManager = ScriptManager
    var customFontManager = FontManager()
    var guiManager = GUIManager()
    val keyBindManager = KeyBindManager

    // HUD & ClickGUI
    var hud = HUD

    val clickGui = ClickGui

    // Menu Background
    var background: Background? = null

    // Discord RPC
    var discordRPC = DiscordRPC

    /**
     * Start IO tasks
     */
    fun preload(): CompletableFuture<*> {
        me.rein.predscan.utils.client.javaVersion

        // Change theme of Swing
        UIManager.setLookAndFeel(FlatMacLightLaf())

        return SharedScopes.IO.future {
            LOGGER.info("Starting preload tasks of $CLIENT_NAME")

            // Download and extract fonts
            runCatching { Fonts.downloadFonts() }.onFailure { LOGGER.warn("Failed font preload step", it) }

            // Check update
            runCatching { ClientUpdate.reloadNewestVersion() }.onFailure { LOGGER.warn("Failed update check step", it) }

            // Get MOTD
            runCatching { reloadMessageOfTheDay() }.onFailure { LOGGER.warn("Failed MOTD step", it) }

            // Load languages
            runCatching { loadLanguages() }.onFailure { LOGGER.warn("Failed languages step", it) }

            // Load alt generators
            runCatching { loadActiveGenerators() }.onFailure { LOGGER.warn("Failed alt generators step", it) }

            // Load SRG file
            runCatching { loadSrg() }.onFailure { LOGGER.warn("Failed SRG step", it) }

            LOGGER.info("Preload tasks of $CLIENT_NAME are completed!")
        }
    }

    /**
     * Execute if client will be started
     */
    fun startClient() {
        isStarting = true
        isLoadingConfig = true

        LOGGER.info("Launching...")

        try {
            // Load client fonts
            Fonts.loadFonts()

            // Register listeners
            RotationUtils
            ClientFixes
            CombatManager
            MacroManager
            CapeService
            InventoryUtils
            InventoryManager
            MiniMapRegister
            TickedActions
            MovementUtils
            PacketUtils
            TimerBalanceUtils
            BPSUtils
            WaitTickUtils
            SilentHotbar
            BlinkUtils
            KeyBindManager
            SpotifyIntegration

            // Load settings
            loadSettings(false) {
                LOGGER.info("Successfully loaded ${it.size} settings.")
            }

            // Register commands
            registerCommands()

            // Setup module manager and register modules
            registerModules()

            // API Connecter
            SharedScopes.IO.launch {
                performAllChecksAsync()
            }

            runCatching {
                // Remapper
                loadSrg()

                if (!Remapper.mappingsLoaded) {
                    error("Failed to load SRG mappings.")
                }

                // ScriptManager
                loadScripts()
                enableScripts()
            }.onFailure {
                LOGGER.error("Failed to load scripts.", it)
            }

            // Load configs
            loadAllConfigs()

            // Update client window
            updateClientWindow()

            // Tabs (Only for Forge!)
            if (hasForge()) {
                BlocksTab()
                ExploitsTab()
                HeadsTab()
            }

            // Disable optifine fastrender
            disableFastRender()

            // Load message of the day
            messageOfTheDay?.message?.let { LOGGER.info("Message of the day: $it") }

            // init discord rpc
            discordRPC = DiscordRPC

            customFontManager = FontManager()
            customFontManager.register()

            guiManager = GUIManager()
            guiManager.register()

            // Login into known token if not empty
            if (CapeService.knownToken.isNotBlank()) {
                SharedScopes.IO.launch {
                    runCatching {
                        CapeService.login(CapeService.knownToken)
                    }.onFailure {
                        LOGGER.error("Failed to login into known cape token.", it)
                    }.onSuccess {
                        LOGGER.info("Successfully logged in into known cape token.")
                    }
                }
            }

            // Refresh cape service
            CapeService.refreshCapeCarriers {
                LOGGER.info("Successfully loaded ${it.size} cape carriers.")
            }

            // Load background
            FileManager.loadBackground()
        } catch (e: Exception) {
            LOGGER.error("Failed to start client: ${e.message}")
            e.showErrorPopup()
        } finally {
            // Set is starting status
            isStarting = false

            if (!FileManager.firstStart && FileManager.backedup) {
                SharedScopes.IO.launch {
                    MiscUtils.showMessageDialog("Warning: backup triggered", "Client update detected! Please check the config folder.")
                }
            }

            eventManager.call(StartupEvent)
            LOGGER.info("Successfully started client")
        }
    }

    /**
     * Execute if client will be stopped
     */
    fun stopClient() {
        // Call client shutdown
        eventManager.call(ClientShutdownEvent)

        // Stop all CoroutineScopes
        SharedScopes.stop()

        // Save all available configs
        saveAllConfigs()
    }

}