/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.visual

import me.rein.predscan.event.Render3DEvent
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.ui.font.Fonts
import me.rein.predscan.utils.attack.EntityUtils.isLookingOnEntities
import me.rein.predscan.utils.client.EntityLookup
import me.rein.predscan.utils.extensions.*
import me.rein.predscan.utils.render.RenderUtils.disableGlCap
import me.rein.predscan.utils.render.RenderUtils.enableGlCap
import me.rein.predscan.utils.render.RenderUtils.resetCaps
import net.minecraft.entity.item.EntityTNTPrimed
import org.lwjgl.opengl.GL11.*
import java.awt.Color
import kotlin.math.pow

object TNTTimer : Module("TNTTimer", Category.VISUAL, Category.SubCategory.RENDER_OVERLAY, spacedName = "TNT Timer") {

    private val scale by float("Scale", 3F, 1F..4F)
    private val font by font("Font", Fonts.fontSemibold40)
    private val fontShadow by boolean("Shadow", true)

    private val color by color("Color", Color.WHITE)

    private val maxRenderDistance by int("MaxRenderDistance", 50, 1..200).onChanged { value ->
        maxRenderDistanceSq = value.toDouble().pow(2)
    }

    private val onLook by boolean("OnLook", false)
    private val maxAngleDifference by float("MaxAngleDifference", 5.0f, 5.0f..90f) { onLook }

    private var maxRenderDistanceSq = 0.0
        set(value) {
            field = if (value <= 0.0) maxRenderDistance.toDouble().pow(2) else value
        }

    private val tntEntities by EntityLookup<EntityTNTPrimed>()
        .filter { it.fuse > 0 }
        .filter { mc.thePlayer.getDistanceSqToEntity(it) <= maxRenderDistanceSq }
        .filter { !onLook || isLookingOnEntities(it, maxAngleDifference.toDouble()) }

    val onRender3D = handler<Render3DEvent> {
        for (entity in tntEntities) {
            renderTNTTimer(entity, entity.fuse / 5)
        }
    }

    private fun renderTNTTimer(tnt: EntityTNTPrimed, timeRemaining: Int) {
        val thePlayer = mc.thePlayer ?: return

        val renderManager = mc.renderManager
        val rotateX = if (mc.gameSettings.thirdPersonView == 2) -1.0f else 1.0f

        glPushAttrib(GL_ENABLE_BIT)
        glPushMatrix()

        val (x, y, z) = tnt.interpolatedPosition(tnt.lastTickPos) - renderManager.renderPos

        // Translate to TNT position
        glTranslated(x, y + 1.5f, z)

        glRotatef(-renderManager.playerViewY, 0F, 1F, 0F)
        glRotatef(renderManager.playerViewX * rotateX, 1F, 0F, 0F)

        disableGlCap(GL_LIGHTING, GL_DEPTH_TEST)

        enableGlCap(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        val text = "TNT Explodes in: $timeRemaining"

        val fontRenderer = font

        // Scale
        val scale = ((thePlayer.getDistanceToEntity(tnt) / 4F).coerceAtLeast(1F) / 150F) * scale
        glScalef(-scale, -scale, scale)

        // Draw text
        val width = fontRenderer.getStringWidth(text) * 0.5f
        fontRenderer.drawString(
            text, 1F + -width, if (fontRenderer == Fonts.minecraftFont) 1F else 1.5F, color.rgb, fontShadow
        )

        resetCaps()
        glPopMatrix()
        glPopAttrib()
    }

}