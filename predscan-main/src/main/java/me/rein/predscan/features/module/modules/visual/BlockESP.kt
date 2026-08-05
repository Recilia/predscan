/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.features.module.modules.visual

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import me.rein.predscan.event.Render3DEvent
import me.rein.predscan.event.async.loopSequence
import me.rein.predscan.event.handler
import me.rein.predscan.features.module.Category
import me.rein.predscan.features.module.Module
import me.rein.predscan.utils.block.BlockUtils.getBlockName
import me.rein.predscan.utils.block.BlockUtils.searchBlocks
import me.rein.predscan.utils.block.block
import me.rein.predscan.utils.extensions.component1
import me.rein.predscan.utils.extensions.component2
import me.rein.predscan.utils.extensions.component3
import me.rein.predscan.utils.extensions.eyes
import me.rein.predscan.utils.render.RenderUtils.draw2D
import me.rein.predscan.utils.render.RenderUtils.drawBlockBox
import net.minecraft.block.Block
import net.minecraft.init.Blocks.air
import net.minecraft.util.BlockPos
import java.awt.Color
import java.util.concurrent.ConcurrentHashMap

object BlockESP : Module("BlockESP", Category.VISUAL, Category.SubCategory.RENDER_OVERLAY) {
    private val mode by choices("Mode", arrayOf("Box", "2D"), "Box")
    private val block by block("Block", 168)
    private val radius by int("Radius", 40, 5..120)
    private val blockLimit by int("BlockLimit", 256, 0..2056)

    private val color by color("Color", Color(255, 179, 72))

    private val posList = ConcurrentHashMap.newKeySet<BlockPos>()

    override fun onDisable() {
        posList.clear()
    }

    val onSearch = loopSequence(dispatcher = Dispatchers.Default) {
        val selectedBlock = Block.getBlockById(block)

        if (selectedBlock == null || selectedBlock == air) {
            delay(1000)
            return@loopSequence
        }

        val (x, y, z) = mc.thePlayer?.eyes ?: return@loopSequence
        val radiusSq = radius * radius

        posList.removeIf {
            it.distanceSqToCenter(x, y, z) >= radiusSq || it.block != selectedBlock
        }

        val listSpace = blockLimit - posList.size

        if (listSpace > 0) {
            posList += searchBlocks(radius, setOf(selectedBlock), listSpace).keys
        }

        delay(1000)
    }

    val onRender3D = handler<Render3DEvent> {
        when (mode) {
            "Box" -> posList.forEach { drawBlockBox(it, color, true) }
            "2D" -> posList.forEach { draw2D(it, color.rgb, Color.BLACK.rgb) }
        }
    }

    override val tag
        get() = getBlockName(block)
}