/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.injection.forge.mixins.item;

import me.rein.predscan.utils.rotation.Rotation;
import me.rein.predscan.utils.rotation.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Item.class)
public class MixinItem {

    @Redirect(method = "getMovingObjectPositionFromPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;rotationYaw:F"))
    private float hookCurrentRotationYaw(EntityPlayer instance) {
        Rotation rotation = RotationUtils.INSTANCE.getCurrentRotation();

        if (instance.getGameProfile() != Minecraft.getMinecraft().thePlayer.getGameProfile() || rotation == null) {
            return instance.rotationYaw;
        }

        return rotation.getYaw();
    }

    @Redirect(method = "getMovingObjectPositionFromPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;rotationPitch:F"))
    private float hookCurrentRotationPitch(EntityPlayer instance) {
        Rotation rotation = RotationUtils.INSTANCE.getCurrentRotation();

        if (instance.getGameProfile() != Minecraft.getMinecraft().thePlayer.getGameProfile() || rotation == null) {
            return instance.rotationPitch;
        }

        return rotation.getPitch();
    }
}