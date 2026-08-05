/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.ui.client.spotify

import me.rein.predscan.event.Event

/**
 * Fired whenever the Spotify connection state changes.
 */
class SpotifyConnectionChangedEvent(
    val state: SpotifyConnectionState,
    val errorMessage: String? = null,
) : Event()

/**
 * Fired whenever the playback information is updated.
 */
class SpotifyStateChangedEvent(val state: SpotifyState?) : Event()
