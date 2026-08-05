/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.utils.timing

import me.rein.predscan.event.GameTickEvent
import me.rein.predscan.event.Listenable
import me.rein.predscan.event.handler
import me.rein.predscan.utils.client.ClientUtils
import me.rein.predscan.utils.client.MinecraftInstance
import me.rein.predscan.utils.kotlin.removeEach

//@Deprecated("Use TickScheduler instead")
object WaitTickUtils : MinecraftInstance, Listenable {

    private val scheduledActions = ArrayDeque<ScheduledAction>()

    inline fun schedule(ticks: Int, requester: Any? = null, crossinline action: () -> Unit = { }) =
        conditionalSchedule(requester, ticks, false) { action(); null }

    fun conditionalSchedule(
        requester: Any? = null,
        ticks: Int? = null,
        isConditional: Boolean = true,
        action: (tick: Int) -> Boolean?
    ) {
        if (ticks == 0) {
            action(0)

            return
        }

        val time = ticks ?: 0

        scheduledActions += ScheduledAction(requester, time, isConditional, ClientUtils.runTimeTicks + time, action)
    }

    fun hasScheduled(obj: Any) = scheduledActions.any { it.requester == obj }

    val onTick = handler<GameTickEvent>(priority = -1) {
        val currentTick = ClientUtils.runTimeTicks

        scheduledActions.removeEach { action ->
            val elapsed = action.duration - (action.ticks - currentTick)
            val shouldRemove = currentTick >= action.ticks

            if (!action.isConditional) {
                if (shouldRemove) {
                    action.action(elapsed) ?: true
                } else {
                    false
                }
            } else {
                action.action(elapsed) ?: shouldRemove
            }
        }
    }

    private data class ScheduledAction(
        val requester: Any?,
        val duration: Int,
        val isConditional: Boolean,
        val ticks: Int,
        val action: (tick: Int) -> Boolean?
    )

}