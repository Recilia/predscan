/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.speedmodes.other

import me.rein.predscan.features.module.modules.movement.Speed
import me.rein.predscan.features.module.modules.movement.Speed.customAirStrafe
import me.rein.predscan.features.module.modules.movement.Speed.customAirTimer
import me.rein.predscan.features.module.modules.movement.Speed.customAirTimerTick
import me.rein.predscan.features.module.modules.movement.Speed.customGroundStrafe
import me.rein.predscan.features.module.modules.movement.Speed.customGroundTimer
import me.rein.predscan.features.module.modules.movement.Speed.customY
import me.rein.predscan.features.module.modules.movement.Speed.notOnConsuming
import me.rein.predscan.features.module.modules.movement.Speed.notOnFalling
import me.rein.predscan.features.module.modules.movement.Speed.notOnVoid
import me.rein.predscan.features.module.modules.movement.speedmodes.SpeedMode
import me.rein.predscan.utils.movement.MovementUtils.strafe
import me.rein.predscan.utils.extensions.isMoving
import me.rein.predscan.utils.extensions.stopXZ
import me.rein.predscan.utils.extensions.stopY
import me.rein.predscan.utils.extensions.tryJump
import me.rein.predscan.utils.movement.FallingPlayer
import net.minecraft.item.ItemBucketMilk
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemPotion

object CustomSpeed : SpeedMode("Custom") {

    override fun onMotion() {
        val player = mc.thePlayer ?: return
        val heldItem = player.heldItem

        val fallingPlayer = FallingPlayer()
        if (notOnVoid && fallingPlayer.findCollision(500) == null
            || notOnFalling && player.fallDistance > 2.5f
            || notOnConsuming && player.isUsingItem
            && (heldItem.item is ItemFood
                    || heldItem.item is ItemPotion
                    || heldItem.item is ItemBucketMilk)
        ) {

            if (player.onGround) player.tryJump()
            mc.timer.timerSpeed = 1f
            return
        }

        if (player.isMoving) {
            if (player.onGround) {
                if (customGroundStrafe > 0) {
                    strafe(customGroundStrafe)
                }

                mc.timer.timerSpeed = customGroundTimer
                player.motionY = customY.toDouble()
            } else {
                if (customAirStrafe > 0) {
                    strafe(customAirStrafe)
                }

                if (player.ticksExisted % customAirTimerTick == 0) {
                    mc.timer.timerSpeed = customAirTimer
                } else {
                    mc.timer.timerSpeed = 1f
                }
            }
        }
    }

    override fun onEnable() {
        val player = mc.thePlayer ?: return

        if (Speed.resetXZ) player.stopXZ()
        if (Speed.resetY) player.stopY()

        super.onEnable()
    }

}