/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.injection.forge.mixins.render;

import me.rein.predscan.features.module.modules.visual.CustomModel;
import me.rein.predscan.features.module.modules.visual.Glint;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(LayerArmorBase.class)
public class MixinLayerArmorBase {

    @Inject(method = "doRenderLayer", at = @At("HEAD"), cancellable = true)
    public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount,
                              float partialTicks, float ageInTicks, float netHeadYaw,
                              float headPitch, float scale, CallbackInfo ci) {
        if (CustomModel.INSTANCE.getState() && !CustomModel.INSTANCE.getMode().equals("Female")) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "renderGlint",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V",
                    ordinal = 0
            )
    )
    private void renderGlintColorHook(float red, float green, float blue, float alpha) {
        if (Glint.INSTANCE.getState()) {
            int rgb = Glint.INSTANCE.getGlintColor().getRGB();
            red   = ((rgb >> 16) & 0xFF) / 255.0F;
            green = ((rgb >> 8) & 0xFF) / 255.0F;
            blue  = (rgb & 0xFF) / 255.0F;
            alpha = ((rgb >> 24) & 0xFF) / 255.0F;
        }
        net.minecraft.client.renderer.GlStateManager.color(red, green, blue, alpha);
    }
}