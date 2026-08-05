/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.player

import me.rein.predscan.event.MoveEvent
import me.rein.predscan.event.UpdateEvent
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.utils.movement.MovementUtils.serverOnGround
import me.rein.predscan.utils.client.PacketUtils.sendPacket
import me.rein.predscan.utils.inventory.ItemUtils.isConsumingItem
import me.rein.predscan.utils.timing.MSTimer
import me.rein.predscan.event.handler
import net.minecraft.network.play.client.C03PacketPlayer

object FastUse : Module("FastUse", Category.PLAYER, Category.SubCategory.PLAYER_COUNTER) {

    private val mode by choices("Mode", arrayOf("Instant", "NCP", "AAC", "Custom"), "NCP")

    private val delay by int("CustomDelay", 0, 0..300) { mode == "Custom" }
    private val customSpeed by int("CustomSpeed", 2, 1..35) { mode == "Custom" }
    private val customTimer by float("CustomTimer", 1.1f, 0.5f..2f) { mode == "Custom" }

    private val noMove by boolean("NoMove", false)

    private val msTimer = MSTimer()
    private var usedTimer = false


    val onUpdate = handler<UpdateEvent> {
        val thePlayer = mc.thePlayer ?: return@handler

        if (usedTimer) {
            mc.timer.timerSpeed = 1F
            usedTimer = false
        }

        if (!isConsumingItem()) {
            msTimer.reset()
            return@handler
        }

        when (mode.lowercase()) {
            "instant" -> {
                repeat(35) {
                    sendPacket(C03PacketPlayer(serverOnGround))
                }

                mc.playerController.onStoppedUsingItem(thePlayer)
            }

            "ncp" -> if (thePlayer.itemInUseDuration > 14) {
                repeat(20) {
                    sendPacket(C03PacketPlayer(serverOnGround))
                }

                mc.playerController.onStoppedUsingItem(thePlayer)
            }

            "aac" -> {
                mc.timer.timerSpeed = 1.22F
                usedTimer = true
            }

            "custom" -> {
                mc.timer.timerSpeed = customTimer
                usedTimer = true

                if (!msTimer.hasTimePassed(delay))
                    return@handler

                repeat(customSpeed) {
                    sendPacket(C03PacketPlayer(serverOnGround))
                }

                msTimer.reset()
            }
        }
    }


    val onMove = handler<MoveEvent> { event ->
         mc.thePlayer ?: return@handler

        if (!isConsumingItem() || !noMove)
            return@handler

        event.zero()
    }

    override fun onDisable() {
        if (usedTimer) {
            mc.timer.timerSpeed = 1F
            usedTimer = false
        }
    }

    override val tag
        get() = mode
}
