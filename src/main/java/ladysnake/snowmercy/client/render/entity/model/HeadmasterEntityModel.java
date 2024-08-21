package ladysnake.snowmercy.client.render.entity.model;

import ladysnake.snowmercy.common.SnowMercy;
import ladysnake.snowmercy.common.entity.HeadmasterEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class HeadmasterEntityModel extends GeoModel<HeadmasterEntity> {
    @Override
    public ResourceLocation getModelResource(HeadmasterEntity object) {
        return new ResourceLocation(SnowMercy.MODID, "geo/headmaster.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HeadmasterEntity object) {
        return new ResourceLocation(SnowMercy.MODID, "textures/entity/headmaster.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HeadmasterEntity animatable) {
        return new ResourceLocation(SnowMercy.MODID, "animations/headmaster.animations.json");
    }

    @SuppressWarnings("removal")
	@Override
    public void setCustomAnimations(HeadmasterEntity headmaster, long uniqueID, AnimationState customPredicate) {
        super.setCustomAnimations(headmaster, uniqueID, customPredicate);
        EntityModelData extraData = (EntityModelData) customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);

        if (!headmaster.isTurret()) {
            CoreGeoBone head = this.getAnimationProcessor().getBone("head");
            CoreGeoBone torso = this.getAnimationProcessor().getBone("torso");

            head.setRotX(extraData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F));

            torso.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F) * 0.25f);
        } else {
        	CoreGeoBone pretorso = this.getAnimationProcessor().getBone("pretorso");

            pretorso.setRotX(extraData.headPitch() * ((float) Math.PI / 180F));
            pretorso.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }
}
