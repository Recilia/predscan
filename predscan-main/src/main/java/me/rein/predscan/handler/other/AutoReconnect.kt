/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.handler.other

import me.rein.predscan.handler.payload.ClientFixes

object AutoReconnect {
    const val MAX = 60000
    const val MIN = 1000

    var isEnabled = true
        internal set

    var delay by ClientFixes.autoReconnectDelayValue
}