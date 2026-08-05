/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.utils.extensions

import java.awt.Color

fun Color.withAlpha(a: Int) = Color(red, green, blue, a)