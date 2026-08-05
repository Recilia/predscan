/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.file

import com.google.gson.JsonObject

abstract class ConfigSection(val sectionName: String) {
    abstract fun load(json: JsonObject)

    abstract fun save(): JsonObject
}