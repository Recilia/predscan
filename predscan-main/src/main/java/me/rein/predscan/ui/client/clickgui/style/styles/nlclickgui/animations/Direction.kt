package me.rein.predscan.ui.client.clickgui.style.styles.nlclickgui.animations

enum class Direction {
    FORWARDS,
    BACKWARDS;

    fun opposite(): Direction = if (this == FORWARDS) BACKWARDS else FORWARDS
}
