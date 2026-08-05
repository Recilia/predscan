/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.other

import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.utils.extensions.rotation
import me.rein.predscan.utils.rotation.AlwaysRotationSettings
import me.rein.predscan.utils.rotation.Rotation
import me.rein.predscan.utils.rotation.RotationUtils.currentRotation
import me.rein.predscan.utils.rotation.RotationUtils.setTargetRotation
import me.rein.predscan.utils.timing.WaitTickUtils
import net.minecraft.entity.player.EntityPlayer

object NoRotateSet : Module("NoRotateSet", Category.OTHER, Category.SubCategory.MISCELLANEOUS, gameDetecting = false) {
    var savedRotation = Rotation.ZERO

    private val ignoreOnSpawn by boolean("IgnoreOnSpawn", false)
    val affectRotation by boolean("AffectRotation", true)

    private val ticksUntilStart = intRange("TicksUntilStart", 0..0, 0..20) { affectRotation }

    private val options = AlwaysRotationSettings(this) { affectRotation }.apply {
        withoutKeepRotation()
        applyServerSideValue.hideWithState(true)
        resetTicksValue.excludeWithState(1)
    }

    fun shouldModify(player: EntityPlayer) = handleEvents() && (!ignoreOnSpawn || player.ticksExisted != 0)

    fun rotateBackToPlayerRotation() {
        val player = mc.thePlayer ?: return

        currentRotation = player.rotation

        WaitTickUtils.schedule(ticksUntilStart.random, this)

        setTargetRotation(savedRotation, options = options)
    }
}