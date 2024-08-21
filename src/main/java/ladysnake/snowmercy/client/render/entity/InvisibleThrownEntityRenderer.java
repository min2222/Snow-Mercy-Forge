package ladysnake.snowmercy.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ThrowableProjectile;

public class InvisibleThrownEntityRenderer extends EntityRenderer<ThrowableProjectile> {
    public InvisibleThrownEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrowableProjectile entity) {
        return null;
    }
}
