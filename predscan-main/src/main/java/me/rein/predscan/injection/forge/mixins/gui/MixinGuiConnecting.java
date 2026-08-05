/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.injection.forge.mixins.gui;

import me.rein.predscan.features.module.modules.client.HUDModule;
import me.rein.predscan.ui.font.Fonts;
import me.rein.predscan.utils.client.ServerUtils;
import me.rein.predscan.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
@SideOnly(Side.CLIENT)
public abstract class MixinGuiConnecting extends GuiScreen {

    @Inject(method = "connect", at = @At("HEAD"))
    private void headConnect(final String ip, final int port, CallbackInfo callbackInfo) {
        ServerUtils.INSTANCE.setServerData(new ServerData("", ip + ":" + port, false));
    }

    /**
     * @author CCBlueX
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        drawDefaultBackground();

        RenderUtils.INSTANCE.drawLoadingCircle(scaledResolution.getScaledWidth() / 2f, scaledResolution.getScaledHeight() / 4f + 70);

        String ip = "Unknown";

        final ServerData serverData = mc.getCurrentServerData();
        if (serverData != null) {
            ip = ServerUtils.INSTANCE.hideSensitiveInformation(serverData.serverIP);
        }

        Fonts.fontSemibold35.drawCenteredString("Connecting to", scaledResolution.getScaledWidth() / 2f, scaledResolution.getScaledHeight() / 4f + 110, 0xFFFFFF, true);
        Fonts.fontSemibold35.drawCenteredString(ip, scaledResolution.getScaledWidth() / 2f, scaledResolution.getScaledHeight() / 4f + 120, HUDModule.INSTANCE.getGuiColor(), true);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}