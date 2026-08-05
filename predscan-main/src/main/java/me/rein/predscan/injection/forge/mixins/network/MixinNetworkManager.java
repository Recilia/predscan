/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.injection.forge.mixins.network;

import io.netty.channel.ChannelHandlerContext;
import me.rein.predscan.event.EventManager;
import me.rein.predscan.event.EventState;
import me.rein.predscan.event.PacketEvent;
import me.rein.predscan.features.module.modules.client.TabGUIModule;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import me.rein.predscan.utils.client.PPSCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void read(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
        final PacketEvent event = new PacketEvent(packet, EventState.RECEIVE);
        EventManager.INSTANCE.call(event);

        if (event.isCancelled()) {
            callback.cancel();
            return;
        }

        PPSCounter.INSTANCE.registerType(PPSCounter.PacketType.RECEIVED);
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, CallbackInfo callback) {
        final PacketEvent event = new PacketEvent(packet, EventState.SEND);
        EventManager.INSTANCE.call(event);

        if (event.isCancelled()) {
            callback.cancel();
            return;
        }

        PPSCounter.INSTANCE.registerType(PPSCounter.PacketType.SEND);
    }


    /**
     * show player head in tab bar
     */
    @Inject(method = "getIsencrypted", at = @At("HEAD"), cancellable = true)
    private void getIsencrypted(CallbackInfoReturnable<Boolean> cir) {
        if(TabGUIModule.INSTANCE.getFlagRenderTabOverlay()) {
            cir.setReturnValue(true);
        }
    }
}