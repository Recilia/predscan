/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.handler.payload;

public class ClientBrandRetriever {

    public static String getClientModName() {
        return ClientFixes.getClientModName();
    }
}
