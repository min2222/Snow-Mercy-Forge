package ladysnake.snowmercy.client.render.entity;

import ladysnake.snowmercy.client.render.WeaponizedSnowmanPumpkinFeatureRenderer;
import ladysnake.snowmercy.client.render.entity.model.WeaponizedSnowGolemEntityModel;
import ladysnake.snowmercy.common.entity.WeaponizedSnowGolemEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WeaponizedSnowGolemEntityRenderer<E extends WeaponizedSnowGolemEntity, M extends WeaponizedSnowGolemEntityModel<E>> extends MobRenderer<E, M> {
    private final ResourceLocation texture;

    public WeaponizedSnowGolemEntityRenderer(EntityRendererProvider.Context context, M entityModel, float shadowRadius, ResourceLocation texture) {
        super(context, entityModel, shadowRadius);
        this.texture = texture;
        this.addLayer(new WeaponizedSnowmanPumpkinFeatureRenderer<>(this));
    }

    public ResourceLocation getTextureLocation(WeaponizedSnowGolemEntity snowGolemEntity) {
        return texture;
    }
}
