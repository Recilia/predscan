/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.nowebmodes

import me.rein.predscan.utils.client.MinecraftInstance

open class NoWebMode(val modeName: String) : MinecraftInstance {
	open fun onUpdate() {}
}
