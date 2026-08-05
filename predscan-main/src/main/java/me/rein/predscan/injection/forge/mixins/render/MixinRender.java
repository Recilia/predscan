/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.injection.forge.mixins.render;

import me.rein.predscan.event.EventManager;
import me.rein.predscan.event.RenderEntityEvent;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Render.class)
public abstract class MixinRender {
    @Shadow
    protected abstract <T extends Entity> boolean bindEntityTexture(T entity);

    @Shadow
    protected void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        EventManager.INSTANCE.call(new RenderEntityEvent(entity, x, y, z, entityYaw, partialTicks));
    }
}
