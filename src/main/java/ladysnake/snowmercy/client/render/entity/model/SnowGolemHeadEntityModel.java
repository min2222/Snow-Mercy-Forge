package ladysnake.snowmercy.client.render.entity.model;

import ladysnake.snowmercy.common.entity.SnowGolemHeadEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SnowGolemHeadEntityModel extends WeaponizedSnowGolemEntityModel<SnowGolemHeadEntity> {
    protected ModelPart rockets_head;
    protected ModelPart mortars_head;
    protected ModelPart saw_head;
    protected ModelPart boombox_head;

    public SnowGolemHeadEntityModel(ModelPart root) {
        super(root, true);
        this.head = root.getChild(PartNames.HEAD);
        this.rockets_head = root.getChild("rockets_head");
        this.mortars_head = root.getChild("mortars_head");
        this.saw_head = root.getChild("saw_head");
        this.boombox_head = root.getChild("boombox_head");
    }

    public static LayerDefinition getTexturedModelData() {
    	MeshDefinition modelData = new MeshDefinition();
    	PartDefinition modelPartData = modelData.getRoot();
    	CubeDeformation dilation = new CubeDeformation(-0.5F);

        modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 7.0F, 8.0F, dilation), PartPose.offset(0.0F, 24.0f, 0.0F));
        modelPartData.getChild(PartNames.HEAD).addOrReplaceChild("tnt", CubeListBuilder.create().texOffs(42, 3).mirror(true).addBox(-0.5F, -28.0F, -0.75F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)).texOffs(48, 0).addBox(-2.0F, -26.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 14.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        modelPartData.addOrReplaceChild("rockets_head", CubeListBuilder.create().mirror(true).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)).texOffs(34, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 24.0f, 0.0F, 0.0F, 0.0F, 0.0F));

        modelPartData.addOrReplaceChild("boombox_head", CubeListBuilder.create().mirror(true).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)).texOffs(34, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 24.0f, 0.0F, 0.0F, 0.0F, 0.0F));

        modelPartData.addOrReplaceChild("mortars_head", CubeListBuilder.create().mirror(true).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 24.0f, 0.0F, 0.0F, 0.0F, 0.0F));
        modelPartData.getChild("mortars_head").addOrReplaceChild("helmet_r1", CubeListBuilder.create().texOffs(32, 0).mirror(true).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        modelPartData.addOrReplaceChild("saw_head", CubeListBuilder.create().mirror(true).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 24.0f, 0.0F, 0.0F, 0.0F, 0.0F));
        modelPartData.getChild("saw_head").addOrReplaceChild("headsaw_r1", CubeListBuilder.create().texOffs(37, 34).mirror(true).addBox(-6.0F, 0.0F, -5.5F, 12.0F, 1.0F, 12.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(-1.0F, -7.0F, -0.5F, 0.0F, 0.0F, 1.1781F));

        return LayerDefinition.create(modelData, 128, 64);
    }

    @Override
    public void setupAnim(SnowGolemHeadEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.yRot = headYaw * 0.017453292F;
        this.head.xRot = headPitch * 0.017453292F;
        this.boombox_head.yRot = headYaw * 0.017453292F;
        this.boombox_head.xRot = headPitch * 0.017453292F;
        this.rockets_head.yRot = headYaw * 0.017453292F;
        this.rockets_head.xRot = headPitch * 0.017453292F;
        this.mortars_head.yRot = headYaw * 0.017453292F;
        this.mortars_head.xRot = headPitch * 0.017453292F;
        this.saw_head.yRot = headYaw * 0.017453292F;
        this.saw_head.xRot = headPitch * 0.017453292F;
        head.visible = false;
        rockets_head.visible = false;
        mortars_head.visible = false;
        saw_head.visible = false;
        boombox_head.visible = false;
        switch (entity.getGolemType()) {
            case SNUGGLES, CHILL_SNUGGLES -> head.visible = true;
            case ROCKETS -> rockets_head.visible = true;
            case MORTARS -> mortars_head.visible = true;
            case SAWMAN -> saw_head.visible = true;
            case BOOMBOX -> boombox_head.visible = true;
        }
    }
}
