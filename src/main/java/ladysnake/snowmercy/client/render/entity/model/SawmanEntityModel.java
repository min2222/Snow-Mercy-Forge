package ladysnake.snowmercy.client.render.entity.model;

import ladysnake.snowmercy.common.entity.WeaponizedSnowGolemEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SawmanEntityModel<T extends WeaponizedSnowGolemEntity> extends WeaponizedSnowGolemEntityModel<T> {
    private final ModelPart saw;

    public SawmanEntityModel(ModelPart root) {
        super(root);
        this.saw = root.getChild("saw");
    }

    public static LayerDefinition getTexturedModelData() {
    	MeshDefinition modelData = new MeshDefinition();
    	PartDefinition modelPartData = modelData.getRoot();

        modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().mirror(true).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        modelPartData.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(0, 16).mirror(true).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 9.0F, 10.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 13.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        modelPartData.addOrReplaceChild("lower_body", CubeListBuilder.create().texOffs(0, 35).mirror(true).addBox(-6.0F, -11.0F, -6.0F, 12.0F, 11.0F, 12.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        modelPartData.getChild(PartNames.HEAD).addOrReplaceChild("headsaw_r1", CubeListBuilder.create().texOffs(37, 34).mirror(true).addBox(-6.0F, 0.0F, -5.5F, 12.0F, 1.0F, 12.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(-1.0F, -7.0F, -0.5F, 0.0F, 0.0F, 1.1781F));
        modelPartData.addOrReplaceChild("saw", CubeListBuilder.create().texOffs(34, 47).mirror(true).addBox(-7.0F, 25.0F, -7.0F, 14.0F, -2.0F, 14.0F, new CubeDeformation(1.0F, 1.0F, 1.0F)).texOffs(32, 10).addBox(-1.5F, 22.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, -11.5F, 0.0F, 0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(modelData, 128, 64);
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        this.saw.yRot = entity.tickCount;
    }
}
