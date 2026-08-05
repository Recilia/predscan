/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.utils.timing

import me.rein.predscan.utils.extensions.safeDiv
import me.rein.predscan.utils.kotlin.RandomUtils.nextInt
import kotlin.math.roundToInt

object TimeUtils {
    fun randomDelay(minDelay: Int, maxDelay: Int) = nextInt(minDelay, maxDelay + 1)

    fun randomClickDelay(minCPS: Int, maxCPS: Int): Int {
        val minDelay = 1000 safeDiv minCPS
        val maxDelay = 1000 safeDiv maxCPS
        return (Math.random() * (minDelay - maxDelay) + maxDelay).roundToInt()
    }
}