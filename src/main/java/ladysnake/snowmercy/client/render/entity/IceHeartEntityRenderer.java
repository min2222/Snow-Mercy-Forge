/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package ladysnake.snowmercy.client.render.entity;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import ladysnake.snowmercy.client.SnowMercyClient;
import ladysnake.snowmercy.client.render.entity.model.IceHeartEntityModel;
import ladysnake.snowmercy.common.SnowMercy;
import ladysnake.snowmercy.common.entity.IceHeartEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;

public class IceHeartEntityRenderer extends EntityRenderer<IceHeartEntity> {
    public static final ResourceLocation BEAM_TEXTURE = new ResourceLocation(SnowMercy.MODID, "textures/entity/ice_heart_beam.png");
    public static final int MAX_BEAM_HEIGHT = 1024;
    private static final ResourceLocation TEXTURE = new ResourceLocation(SnowMercy.MODID, "textures/entity/heart_of_ice.png");
    private final IceHeartEntityModel<IceHeartEntity> model;

    public IceHeartEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0;
        this.shadowStrength = 0;

        this.model = new IceHeartEntityModel<>(context.bakeLayer(SnowMercyClient.HEART_OF_ICE_MODEL_LAYER));
    }

    private static void renderBeam(IceHeartEntity iceHeartEntity, PoseStack matrices, MultiBufferSource vertexConsumers, float tickDelta, long worldTime, int yOffset, int maxY, float[] color) {
        renderBeam(iceHeartEntity, matrices, vertexConsumers, BEAM_TEXTURE, tickDelta, 1.0f, worldTime, yOffset, maxY, color, 0.2f, 0.25f);
    }

    public static void renderBeam(IceHeartEntity iceHeartEntity, PoseStack matrices, MultiBufferSource vertexConsumers, ResourceLocation textureId, float tickDelta, float heightScale, long worldTime, int yOffset, int maxY, float[] color, float innerRadius, float outerRadius) {
        int i = yOffset + maxY;
        matrices.pushPose();
        matrices.translate(0, 0.25, 0);
        float f = (float) Math.floorMod(worldTime, 40) + tickDelta;
        float g = maxY < 0 ? f : -f;
        float h = Mth.frac(g * 0.2f - (float) Mth.floor(g * 0.1f));
        float j = color[0];

        float k = color[1];
        float l = color[2];
        if (iceHeartEntity.isActive()) {
            k = (float) Math.abs(Math.cos(worldTime / 10f));
            l = (float) Math.abs(Math.cos(worldTime / 10f));
        }

        matrices.pushPose();
        matrices.mulPose(Axis.YP.rotationDegrees(f * 2.25f - 45.0f));
        float m = 0.0f;
        float n = innerRadius;
        float o = innerRadius;
        float p = 0.0f;
        float q = -innerRadius;
        float r = 0.0f;
        float s = 0.0f;
        float t = -innerRadius;
        float u = 0.0f;
        float v = 1.0f;
        float w = -1.0f + h;
        float x = (float) maxY * heightScale * (0.5f / innerRadius) + w;
        renderBeamLayer(matrices, vertexConsumers.getBuffer(RenderType.beaconBeam(textureId, false)), j, k, l, 1.0f, yOffset, i, 0.0f, n, o, 0.0f, q, 0.0f, 0.0f, t, 0.0f, 1.0f, x, w);
        matrices.popPose();
        m = -outerRadius;
        n = -outerRadius;
        o = outerRadius;
        p = -outerRadius;
        q = -outerRadius;
        r = outerRadius;
        s = outerRadius;
        t = outerRadius;
        u = 0.0f;
        v = 1.0f;
        w = -1.0f + h;
        x = (float) maxY * heightScale + w;
        renderBeamLayer(matrices, vertexConsumers.getBuffer(RenderType.beaconBeam(textureId, true)), j, k, l, 0.125f, yOffset, i, m, n, o, p, q, r, s, t, 0.0f, 1.0f, x, w);
        matrices.popPose();
    }

    private static void renderBeamLayer(PoseStack matrices, VertexConsumer vertices, float red, float green, float blue, float alpha, int yOffset, int height, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4, float u1, float u2, float v1, float v2) {
    	PoseStack.Pose entry = matrices.last();
        Matrix4f matrix4f = entry.pose();
        Matrix3f matrix3f = entry.normal();
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x1, z1, x2, z2, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x4, z4, x3, z3, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x2, z2, x4, z4, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x3, z3, x1, z1, u1, u2, v1, v2);
    }

    private static void renderBeamFace(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float red, float green, float blue, float alpha, int yOffset, int height, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, height, x1, z1, u2, v1);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, yOffset, x1, z1, u2, v2);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, yOffset, x2, z2, u1, v2);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, height, x2, z2, u1, v1);
    }

    /**
     * @param v the top-most coordinate of the texture region
     * @param u the left-most coordinate of the texture region
     */
    private static void renderBeamVertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float red, float green, float blue, float alpha, int y, float x, float z, float u, float v) {
        vertices.vertex(positionMatrix, x, y, z).color(red, green, blue, alpha).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0.0f, 1.0f, 0.0f).endVertex();
    }

    public void render(IceHeartEntity iceHeartEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

    	MultiBufferSource.BufferSource immediate = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexConsumer2 = immediate.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(iceHeartEntity)));

        float g1 = 1.0f;
        float b1 = 1.0f;
        long l = iceHeartEntity.level.getGameTime();
        if (iceHeartEntity.isActive()) {
            g1 = (float) Math.abs(Math.cos(l / 10f));
            b1 = (float) Math.abs(Math.cos(l / 10f));
        }

        this.model.setupAnim(iceHeartEntity, 0, 0, 0, 0, 0);
        this.model.renderToBuffer(matrixStack, vertexConsumer2, i, OverlayTexture.NO_OVERLAY, 1.0f, g1, b1, 1.0f);
        immediate.endBatch();

        super.render(iceHeartEntity, f, g, matrixStack, vertexConsumerProvider, i);

        List<BeaconBlockEntity.BeaconBeamSection> list = new ArrayList<>();
        list.add(new BeaconBlockEntity.BeaconBeamSection(new float[]{1.0f, 1.0f, 1.0f}));
        int k = 0;
        for (int m = 0; m < list.size(); ++m) {
            BeaconBlockEntity.BeaconBeamSection beamSegment = list.get(m);
            renderBeam(iceHeartEntity, matrixStack, vertexConsumerProvider, f, l, k, m == list.size() - 1 ? 1024 : beamSegment.getHeight(), beamSegment.getColor());
            k += beamSegment.getHeight();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(IceHeartEntity entity) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(IceHeartEntity entity, BlockPos pos) {
        return 15;
    }

    @Override
    public boolean shouldRender(IceHeartEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }

}

