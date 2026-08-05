/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement

import me.rein.predscan.event.UpdateEvent
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.features.module.modules.movement.nowebmodes.aac.AAC
import me.rein.predscan.features.module.modules.movement.nowebmodes.aac.LAAC
import me.rein.predscan.features.module.modules.movement.nowebmodes.grim.OldGrim
import me.rein.predscan.features.module.modules.movement.nowebmodes.intave.IntaveNew
import me.rein.predscan.features.module.modules.movement.nowebmodes.intave.IntaveOld
import me.rein.predscan.features.module.modules.movement.nowebmodes.other.None
import me.rein.predscan.features.module.modules.movement.nowebmodes.other.Rewi

object NoWeb : Module("NoWeb", Category.MOVEMENT, Category.SubCategory.MOVEMENT_MAIN) {

    private val noWebModes = arrayOf(
        // Vanilla
        None,

        // AAC
        AAC, LAAC,

        // Intave
        IntaveOld,
        IntaveNew,

        // Grim
        OldGrim,

        // Other
        Rewi,
    )

    private val modes = noWebModes.map { it.modeName }.toTypedArray()

    val mode by choices(
        "Mode", modes, "None"
    )

    val onUpdate = handler<UpdateEvent> {
        modeModule.onUpdate()
    }

    override val tag
        get() = mode

    private val modeModule
        get() = noWebModes.find { it.modeName == mode }!!
}
