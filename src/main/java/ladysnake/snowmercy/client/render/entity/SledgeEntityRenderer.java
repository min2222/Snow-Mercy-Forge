package ladysnake.snowmercy.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import ladysnake.snowmercy.client.SnowMercyClient;
import ladysnake.snowmercy.client.render.entity.model.SledgeEntityModel;
import ladysnake.snowmercy.common.SnowMercy;
import ladysnake.snowmercy.common.entity.SledgeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SledgeEntityRenderer extends EntityRenderer<SledgeEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(SnowMercy.MODID, "textures/entity/hammersledge.png");
    public static final ResourceLocation TEXTURE_SPEED = new ResourceLocation(SnowMercy.MODID, "textures/entity/hammersledge.png");
    public SledgeEntityModel model;

    public SledgeEntityRenderer(Context context) {
        super(context);
        this.shadowRadius = 0.4f;
        this.model = new SledgeEntityModel(context.bakeLayer(SnowMercyClient.SLEDGE_MODEL_LAYER));
    }

    @Override
    public void render(SledgeEntity sledgeEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.375, 0.0);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0f - f));
        float h = (float) sledgeEntity.getDamageWobbleTicks() - g;
        float j = sledgeEntity.getDamageWobbleStrength() - g;
        if (j < 0.0f) {
            j = 0.0f;
        }
        if (h > 0.0f) {
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(h) * h * j / 10.0f * (float) sledgeEntity.getDamageWobbleSide()));
        }
        matrixStack.scale(-1.0f, -1.0f, 1.0f);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.renderType(TEXTURE));
        this.model.renderToBuffer(matrixStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.popPose();
        super.render(sledgeEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public ResourceLocation getTextureLocation(SledgeEntity entity) {
        return TEXTURE;
    }
}

