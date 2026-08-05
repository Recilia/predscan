/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.flymodes.verus
/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
import me.rein.predscan.features.module.modules.movement.flymodes.FlyMode
import me.rein.predscan.utils.extensions.isInLiquid
import me.rein.predscan.utils.movement.MovementUtils.strafe

/*
* Working on Verus: b3896/b3901
* Tested on: eu.loyisa.cn, anticheat-test.com
*
* Not that useful, but why not ;]
*/
object VerusGlide : FlyMode("VerusGlide") {

    override fun onUpdate() {
        val player = mc.thePlayer ?: return
        if (player.isInLiquid || player.isInWeb || player.isOnLadder) return

        if (!player.onGround && player.fallDistance > 1) {
            // Good job, Verus
            player.motionY = -0.09800000190734863
            if (player.movementInput.moveForward != 0f && player.movementInput.moveStrafe != 0f) {
                strafe(0.334f)
            } else {
                strafe(0.3345f)
            }
        }
    }
}
