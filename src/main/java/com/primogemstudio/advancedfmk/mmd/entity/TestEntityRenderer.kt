package com.primogemstudio.advancedfmk.mmd.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.primogemstudio.advancedfmk.mmd.Loader
import com.primogemstudio.mmdbase.io.PMXFile
import com.primogemstudio.advancedfmk.mmd.renderer.CustomRenderType
import com.primogemstudio.advancedfmk.mmd.renderer.TextureManager
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import org.joml.Matrix4f

class TestEntityRenderer(context: EntityRendererProvider.Context) : EntityRenderer<TestEntity>(context) {
    companion object {
        private val model = Loader.load().second
    }

    override fun getTextureLocation(entity: TestEntity): ResourceLocation {
        return ResourceLocation("")
    }

    override fun shouldRender(
        livingEntity: TestEntity, camera: Frustum, camX: Double, camY: Double, camZ: Double
    ): Boolean {
        return true
    }

    override fun render(
        entity: TestEntity,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int
    ) {
        poseStack.pushPose()
        with(model.textureManager as TextureManager) {
            ids.forEach { i ->
                val buf = buffer.getBuffer(CustomRenderType.mmd(i.value))
                model.m_faces.slice(ranges[i.key] ?: IntRange.EMPTY).forEach{ f ->
                    f.m_vertices.forEach { buf.pmxVertex(poseStack.last().pose(), model, it).endVertex() }
                }
            }
        }
        poseStack.popPose()
    }
}

inline fun VertexConsumer.pmxVertex(mat: Matrix4f, m: PMXFile, i: Int): VertexConsumer {
    val v = m.m_vertices[i].m_position
    val uv = m.m_vertices[i].m_uv
    return this.vertex(mat, v.x, v.y, v.z).uv(uv.x, uv.y)
}