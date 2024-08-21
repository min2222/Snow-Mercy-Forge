package ladysnake.snowmercy.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import ladysnake.snowmercy.common.SnowMercy;
import ladysnake.snowmercy.common.entity.IceballEntity;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class IceballEntityRenderer
        extends MobRenderer<IceballEntity, SlimeModel<IceballEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SnowMercy.MODID, "textures/entity/iceball.png");

    public IceballEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new SlimeModel<IceballEntity>(context.bakeLayer(ModelLayers.SLIME)), 0.25f);
        this.addLayer(new SlimeOuterLayer<IceballEntity>(this, context.getModelSet()));
    }

    @Override
    public void render(IceballEntity IceballEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        this.shadowRadius = 0.25f * (float) IceballEntity.getSize();
        super.render(IceballEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    protected void scale(IceballEntity IceballEntity, PoseStack matrixStack, float f) {
        //float g = 0.999f;
        matrixStack.scale(0.999f, 0.999f, 0.999f);
        matrixStack.translate(0.0, 0.001f, 0.0);
        float h = IceballEntity.getSize();
        float i = Mth.lerp(f, IceballEntity.oSquish, IceballEntity.squish) / (h * 0.5f + 1.0f);
        float j = 1.0f / (i + 1.0f);
        matrixStack.scale(j * h, 1.0f / j * h, j * h);
    }

    @Override
    public ResourceLocation getTextureLocation(IceballEntity IceballEntity) {
        return TEXTURE;
    }
}

