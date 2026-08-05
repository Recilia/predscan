/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.utils.render

import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import me.rein.predscan.utils.render.particle.ParticleGenerator

@SideOnly(Side.CLIENT)
object ParticleUtils {
    private val particleGenerator = ParticleGenerator(100)

    fun drawParticles(mouseX: Int, mouseY: Int) = particleGenerator.draw(mouseX, mouseY)
}