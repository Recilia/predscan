/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.utils.render

import net.minecraft.client.renderer.vertex.VertexBuffer
import net.minecraft.client.renderer.vertex.VertexFormat

/**
 * Like [VertexBuffer], but it deletes its contents when it is deleted
 * by the garbage collector
 */
class SafeVertexBuffer(vertexFormatIn: VertexFormat) : VertexBuffer(vertexFormatIn) {
    protected fun finalize() = deleteGlBuffers()
}