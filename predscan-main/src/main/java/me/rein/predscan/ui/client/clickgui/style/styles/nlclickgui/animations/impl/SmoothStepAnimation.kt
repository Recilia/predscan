package me.rein.predscan.ui.client.clickgui.style.styles.nlclickgui.animations.impl

import me.rein.predscan.ui.client.clickgui.style.styles.nlclickgui.animations.Animation
import me.rein.predscan.ui.client.clickgui.style.styles.nlclickgui.animations.Direction

class SmoothStepAnimation @JvmOverloads constructor(
    ms: Int,
    endPoint: Double,
    direction: Direction = Direction.FORWARDS
) : Animation(ms, endPoint, direction) {

    override fun getEquation(x: Double): Double {
        val x1 = x / duration.toDouble()
        return -2 * Math.pow(x1, 3.0) + 3 * Math.pow(x1, 2.0)
    }
}
