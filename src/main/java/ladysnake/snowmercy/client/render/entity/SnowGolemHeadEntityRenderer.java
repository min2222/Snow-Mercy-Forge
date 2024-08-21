package ladysnake.snowmercy.client.render.entity;

import ladysnake.snowmercy.client.SnowMercyClient;
import ladysnake.snowmercy.client.render.entity.model.SnowGolemHeadEntityModel;
import ladysnake.snowmercy.common.entity.SnowGolemHeadEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SnowGolemHeadEntityRenderer extends MobRenderer<SnowGolemHeadEntity, SnowGolemHeadEntityModel> {
    public SnowGolemHeadEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new SnowGolemHeadEntityModel(context.bakeLayer(SnowMercyClient.SNOW_GOLEM_HEAD_MODEL_LAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(SnowGolemHeadEntity head) {
        return head.getGolemType().getTexture();
    }
}
