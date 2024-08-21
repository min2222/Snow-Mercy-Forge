package ladysnake.snowmercy.client.render.entity;

import ladysnake.snowmercy.common.SnowMercy;
import ladysnake.snowmercy.common.entity.IcicleEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class IcicleEntityRenderer extends ArrowRenderer<IcicleEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(SnowMercy.MODID, "textures/entity/projectiles/icicle.png");

    public IcicleEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(IcicleEntity arrowEntity) {
        return TEXTURE;
    }
}
