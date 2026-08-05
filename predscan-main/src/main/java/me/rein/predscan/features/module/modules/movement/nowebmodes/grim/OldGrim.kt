/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.movement.nowebmodes.grim

import me.rein.predscan.features.module.modules.movement.nowebmodes.NoWebMode
import me.rein.predscan.utils.client.PacketUtils.sendPacket
import me.rein.predscan.utils.block.BlockUtils
import net.minecraft.init.Blocks.web
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action
import net.minecraft.util.EnumFacing

object OldGrim : NoWebMode("OldGrim") {

    override fun onUpdate() {
        val searchBlocks = BlockUtils.searchBlocks(2, setOf(web))
        mc.thePlayer.isInWeb = false
        for (block in searchBlocks) {
            val blockpos = block.key
            sendPacket(C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK,blockpos, EnumFacing.DOWN))
        }
    }
}