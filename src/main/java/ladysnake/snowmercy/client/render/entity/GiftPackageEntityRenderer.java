/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package ladysnake.snowmercy.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import ladysnake.snowmercy.client.SnowMercyClient;
import ladysnake.snowmercy.client.render.entity.model.GiftPackageEntityModel;
import ladysnake.snowmercy.common.SnowMercy;
import ladysnake.snowmercy.common.entity.GiftPackageEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class GiftPackageEntityRenderer extends EntityRenderer<GiftPackageEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SnowMercy.MODID, "textures/entity/gift_package.png");
    private final GiftPackageEntityModel<GiftPackageEntity> model;

    public GiftPackageEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0;
        this.shadowStrength = 0;

        this.model = new GiftPackageEntityModel<>(context.bakeLayer(SnowMercyClient.GIFT_PACKAGE_MODEL_LAYER));

        //Random random = new Random();
    }

    public void render(GiftPackageEntity giftPackageEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        MultiBufferSource.BufferSource immediate = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexConsumer2 = immediate.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(giftPackageEntity)));

        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.translate(0, -0.7, 0);

        this.model.setupAnim(giftPackageEntity, 0, 0, 0, 0, 0);
        this.model.renderToBuffer(matrixStack, vertexConsumer2, i, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1.0f);
        immediate.endBatch();

        super.render(giftPackageEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public ResourceLocation getTextureLocation(GiftPackageEntity entity) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(GiftPackageEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }

}

