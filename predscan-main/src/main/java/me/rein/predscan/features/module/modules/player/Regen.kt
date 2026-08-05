/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.player

import me.rein.predscan.event.UpdateEvent
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.utils.client.PacketUtils.sendPacket
import me.rein.predscan.utils.extensions.isMoving
import me.rein.predscan.utils.movement.MovementUtils.serverOnGround
import me.rein.predscan.utils.timing.MSTimer
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.potion.Potion

object Regen : Module("Regen", Category.PLAYER, Category.SubCategory.PLAYER_COUNTER) {

    private val mode by choices("Mode", arrayOf("Vanilla", "Spartan"), "Vanilla")
    private val speed by int("Speed", 100, 1..100) { mode == "Vanilla" }

    private val delay by int("Delay", 0, 0..10000)
    private val health by int("Health", 18, 0..20)
    private val food by int("Food", 18, 0..20)

    private val noAir by boolean("NoAir", false)
    private val potionEffect by boolean("PotionEffect", false)

    private val timer = MSTimer()

    private var resetTimer = false

    val onUpdate = handler<UpdateEvent> {
        if (resetTimer) {
            mc.timer.timerSpeed = 1F
        } else {
            resetTimer = false
        }

        val thePlayer = mc.thePlayer ?: return@handler

        if (
            !mc.playerController.gameIsSurvivalOrAdventure()
            || noAir && !serverOnGround
            || thePlayer.foodStats.foodLevel <= food
            || !thePlayer.isEntityAlive
            || thePlayer.health >= health
            || (potionEffect && !thePlayer.isPotionActive(Potion.regeneration))
            || !timer.hasTimePassed(delay)
        ) return@handler

        when (mode.lowercase()) {
            "vanilla" -> {
                repeat(speed) {
                    sendPacket(C03PacketPlayer(serverOnGround))
                }
            }

            "spartan" -> {
                if (!thePlayer.isMoving && serverOnGround) {
                    repeat(9) {
                        sendPacket(C03PacketPlayer(serverOnGround))
                    }

                    mc.timer.timerSpeed = 0.45F
                    resetTimer = true
                }
            }
        }

        timer.reset()
    }
}
