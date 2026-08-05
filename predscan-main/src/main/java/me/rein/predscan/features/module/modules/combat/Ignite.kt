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
import me.rein.predscan.utils.attack.EntityUtils
import me.rein.predscan.utils.block.block
import me.rein.predscan.utils.block.canBeClicked
import me.rein.predscan.utils.block.isReplaceable
import me.rein.predscan.utils.client.PacketUtils.sendPacket
import me.rein.predscan.utils.extensions.onPlayerRightClick
import me.rein.predscan.utils.extensions.sendUseItem
import me.rein.predscan.utils.extensions.toDegreesF
import me.rein.predscan.utils.inventory.InventoryUtils
import me.rein.predscan.utils.inventory.SilentHotbar
import me.rein.predscan.utils.inventory.hotBarSlot
import me.rein.predscan.utils.rotation.RotationUtils
import me.rein.predscan.utils.timing.MSTimer
import net.minecraft.block.BlockAir
import net.minecraft.init.Items
import net.minecraft.item.ItemBucket
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook
import net.minecraft.util.EnumFacing
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3
import kotlin.math.atan2
import kotlin.math.sqrt

// TODO: This desperately needs a recode
object Ignite : Module("Ignite", Category.COMBAT, Category.SubCategory.COMBAT_LEGIT) {

    private val lighter by boolean("Lighter", true)
    private val lavaBucket by boolean("Lava", true)

    private val msTimer = MSTimer()

    val onUpdate = handler<UpdateEvent> {
        if (!msTimer.hasTimePassed(500))
            return@handler

        val player = mc.thePlayer ?: return@handler
        val world = mc.theWorld ?: return@handler

        val lighterInHotbar = if (lighter) InventoryUtils.findItem(36, 44, Items.flint_and_steel) else null
        val lavaInHotbar = if (lavaBucket) InventoryUtils.findItem(36, 44, Items.lava_bucket) else null

        val fireInHotbar = lighterInHotbar ?: lavaInHotbar ?: return@handler

        for (entity in world.loadedEntityList) {
            if (EntityUtils.isSelected(entity, true) && !entity.isBurning) {
                val blockPos = entity.position

                if (player.getDistanceSq(blockPos) >= 22.3 || !blockPos.isReplaceable || blockPos.block !is BlockAir)
                    continue

                RotationUtils.resetTicks += 1

                SilentHotbar.selectSlotSilently(this, fireInHotbar, 0, immediate = true, render = false)

                val itemStack = player.hotBarSlot(fireInHotbar).stack

                if (itemStack.item is ItemBucket) {
                    val diffX = blockPos.x + 0.5 - player.posX
                    val diffY = blockPos.y + 0.5 - (player.entityBoundingBox.minY + player.eyeHeight)
                    val diffZ = blockPos.z + 0.5 - player.posZ
                    val sqrt = sqrt(diffX * diffX + diffZ * diffZ)
                    val yaw = (atan2(diffZ, diffX)).toDegreesF() - 90F
                    val pitch = -(atan2(diffY, sqrt)).toDegreesF()

                    sendPacket(
                        C05PacketPlayerLook(
                            player.rotationYaw +
                                    MathHelper.wrapAngleTo180_float(yaw - player.rotationYaw),
                            player.rotationPitch +
                                    MathHelper.wrapAngleTo180_float(pitch - player.rotationPitch),
                            player.onGround
                        )
                    )

                    player.sendUseItem(itemStack)
                } else {
                    for (side in EnumFacing.entries) {
                        val neighbor = blockPos.offset(side)

                        if (!neighbor.canBeClicked())
                            continue

                        val diffX = neighbor.x + 0.5 - player.posX
                        val diffY = neighbor.y + 0.5 - (player.entityBoundingBox.minY + player.eyeHeight)
                        val diffZ = neighbor.z + 0.5 - player.posZ
                        val sqrt = sqrt(diffX * diffX + diffZ * diffZ)
                        val yaw = (atan2(diffZ, diffX)).toDegreesF() - 90F
                        val pitch = -(atan2(diffY, sqrt)).toDegreesF()

                        sendPacket(
                            C05PacketPlayerLook(
                                player.rotationYaw +
                                        MathHelper.wrapAngleTo180_float(yaw - player.rotationYaw),
                                player.rotationPitch +
                                        MathHelper.wrapAngleTo180_float(pitch - player.rotationPitch),
                                player.onGround
                            )
                        )

                        if (player.onPlayerRightClick(neighbor, side.opposite, Vec3(side.directionVec), itemStack)) {
                            player.swingItem()
                            break
                        }
                    }
                }

                SilentHotbar.selectSlotSilently(
                    this,
                    player.inventory.currentItem,
                    immediate = true,
                    render = false,
                    resetManually = true
                )
                sendPacket(C05PacketPlayerLook(player.rotationYaw, player.rotationPitch, player.onGround))
                SilentHotbar.resetSlot(this)

                msTimer.reset()
                break
            }
        }
    }
}
