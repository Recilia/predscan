/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.handler.api

import me.rein.predscan.PredScan
import me.rein.predscan.PredScan.IN_DEV
import me.rein.predscan.PredScan.clientVersionNumber
import me.rein.predscan.utils.client.ClientUtils.LOGGER
import java.text.SimpleDateFormat
import java.util.*

object ClientUpdate {

    val gitInfo = Properties().also {
        PredScan::class.java.classLoader
            .getResourceAsStream("git.properties")
            ?.use(it::load) ?: run {
                it["git.build.version"] = "unofficial"
            }
    }

    fun reloadNewestVersion() {
        // https://api.liquidbounce.net/api/v1/version/builds/legacy
        try {
            newestVersion = ClientApi.getNewestBuild(release = !IN_DEV)
        } catch (e: Exception) {
            LOGGER.error("Unable to receive update information", e)
        }
    }

    var newestVersion: Build? = null
        private set

    fun hasUpdate(): Boolean {
        try {
            val newestVersion = newestVersion ?: return false
            val actualVersionNumber =
                newestVersion.lbVersion.removePrefix("b").toIntOrNull() ?: 0 // version format: "b<VERSION>" on legacy

            return if (IN_DEV) { // check if new build is newer than current build
                val newestVersionDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(newestVersion.date)
                val currentVersionDate =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(gitInfo["git.commit.time"].toString())

                newestVersionDate.after(currentVersionDate)
            } else {
                // check if version number is higher than current version number (on release builds only!)
                newestVersion.release && actualVersionNumber > clientVersionNumber
            }
        } catch (e: Exception) {
            LOGGER.error("Unable to check for update", e)
            return false
        }
    }

}

