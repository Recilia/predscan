/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.combat

import me.rein.predscan.event.UpdateEvent
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.utils.client.PacketUtils.sendPacket
import net.minecraft.item.ItemBow
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action.RELEASE_USE_ITEM
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing

object AutoBow : Module("AutoBow", Category.COMBAT, Category.SubCategory.COMBAT_LEGIT, subjective = true) {

    private val waitForBowAimbot by boolean("WaitForBowAimbot", true)

    val onUpdate = handler<UpdateEvent> {
        val thePlayer = mc.thePlayer

        if (thePlayer.isUsingItem && thePlayer.heldItem?.item is ItemBow && thePlayer.itemInUseDuration > 20
            && (!waitForBowAimbot || !ProjectileAimbot.handleEvents() || ProjectileAimbot.hasTarget())
        ) {
            thePlayer.stopUsingItem()
            sendPacket(C07PacketPlayerDigging(RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN))
        }
    }
}
