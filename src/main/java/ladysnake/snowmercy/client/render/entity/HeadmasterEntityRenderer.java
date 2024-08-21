package ladysnake.snowmercy.client.render.entity;

import ladysnake.snowmercy.client.render.entity.model.HeadmasterEntityModel;
import ladysnake.snowmercy.common.entity.HeadmasterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HeadmasterEntityRenderer extends GeoEntityRenderer<HeadmasterEntity> {
    public HeadmasterEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new HeadmasterEntityModel());
        this.shadowRadius = 1.2F;
    }
}
