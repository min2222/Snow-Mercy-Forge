package ladysnake.snowmercy.client.render.entity.model;

import ladysnake.snowmercy.common.entity.WeaponizedSnowGolemEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class WeaponizedSnowGolemEntityModel<T extends WeaponizedSnowGolemEntity> extends HierarchicalModel<T> {
    protected ModelPart root;
    protected ModelPart upperBody;
    protected ModelPart head;

    public WeaponizedSnowGolemEntityModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild(PartNames.HEAD);
        this.upperBody = root.getChild("upper_body");
    }

    public WeaponizedSnowGolemEntityModel(ModelPart root, boolean bl) {
        this.root = root;
    }

    public static LayerDefinition getLayerDefinition() {
    	MeshDefinition modelData = new MeshDefinition();
        PartDefinition PartDefinition = modelData.getRoot();
        CubeDeformation CubeDeformation = new CubeDeformation(-0.5F);
        PartDefinition.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, CubeDeformation), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(0, 16).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, CubeDeformation), PartPose.offset(0.0F, 13.0F, 0.0F));
        PartDefinition.addOrReplaceChild("lower_body", CubeListBuilder.create().texOffs(0, 36).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, CubeDeformation), PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    public static LayerDefinition boomboxModelData() {
    	MeshDefinition modelData = new MeshDefinition();
        PartDefinition PartDefinition = modelData.getRoot();

        PartDefinition.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().mirror(true).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)).texOffs(34, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(0, 16).mirror(true).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 13.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition.addOrReplaceChild("lower_body", CubeListBuilder.create().texOffs(0, 36).mirror(true).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(modelData, 128, 64);
    }

    public static LayerDefinition rocketsModelData() {
    	MeshDefinition modelData = new MeshDefinition();
        PartDefinition PartDefinition = modelData.getRoot();

        PartDefinition.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().mirror(true).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)).texOffs(34, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(0, 16).mirror(true).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 13.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition.addOrReplaceChild("lower_body", CubeListBuilder.create().texOffs(0, 36).mirror(true).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition.getChild("upper_body").addOrReplaceChild("launcher", CubeListBuilder.create().texOffs(40, 23).mirror(true).addBox(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 8.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(-6.5F, -6.5F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition.getChild("upper_body").getChild("launcher").addOrReplaceChild("rocket", CubeListBuilder.create().texOffs(66, 25).mirror(true).addBox(-1.5F, -1.5F, -4.0F, 3.0F, 3.0F, 8.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition.getChild("upper_body").getChild("launcher").addOrReplaceChild("tip", CubeListBuilder.create().texOffs(88, 28).mirror(true).addBox(-0.5F, -2.5F, -7.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition.getChild("upper_body").getChild("launcher").addOrReplaceChild("tip_r1", CubeListBuilder.create().texOffs(88, 28).mirror(true).addBox(-0.5F, -2.5F, 0.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, 0.0F, 0.0F, -1.5708F));
        PartDefinition.getChild("upper_body").getChild("launcher").addOrReplaceChild("rocket2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -12.0F, 0.0F, 0.0873F, 0.0F));
        PartDefinition.getChild("upper_body").getChild("launcher").addOrReplaceChild("tip2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition.getChild("upper_body").addOrReplaceChild("launcher2", CubeListBuilder.create().texOffs(40, 23).mirror(true).addBox(-2.5F, -2.5F, -3.0F, 5.0F, 5.0F, 8.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(6.5F, -6.5F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition.getChild("upper_body").getChild("launcher2").addOrReplaceChild("rocket3", CubeListBuilder.create().texOffs(66, 25).mirror(true).addBox(-1.5F, -1.5F, -4.0F, 3.0F, 3.0F, 8.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition.getChild("upper_body").getChild("launcher2").addOrReplaceChild("tip3", CubeListBuilder.create().texOffs(88, 28).mirror(true).addBox(-0.5F, -2.5F, -7.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition.getChild("upper_body").getChild("launcher2").addOrReplaceChild("tip_r2", CubeListBuilder.create().texOffs(88, 28).mirror(true).addBox(-0.5F, -2.5F, 0.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, 0.0F, 0.0F, -1.5708F));
        PartDefinition.getChild("upper_body").getChild("launcher2").addOrReplaceChild("rocket4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -12.0F, 0.0F, 0.0873F, 0.0F));
        PartDefinition.getChild("upper_body").getChild("launcher2").addOrReplaceChild("tip4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(modelData, 128, 64);
    }

    public static LayerDefinition snugglesModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition PartDefinition = modelData.getRoot();
        CubeDeformation CubeDeformation = new CubeDeformation(-0.5F);
        PartDefinition.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 7.0F, 8.0F, CubeDeformation), PartPose.offset(0.0F, 4.0F, 0.0F));
        PartDefinition.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(0, 15).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, CubeDeformation), PartPose.offset(0.0F, 13.0F, 0.0F));
        PartDefinition.addOrReplaceChild("lower_body", CubeListBuilder.create().texOffs(0, 35).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, CubeDeformation), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition.getChild(PartNames.HEAD).addOrReplaceChild("tnt", CubeListBuilder.create().texOffs(42, 3).mirror(true).addBox(-0.5F, -28.0F, -0.75F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)).texOffs(48, 0).addBox(-2.0F, -26.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.5F, -0.5F, -0.5F)), PartPose.offsetAndRotation(0.0F, 14.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(modelData, 128, 64);
    }

    public static LayerDefinition mortarsModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition PartDefinition = modelData.getRoot();

        PartDefinition head = PartDefinition.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offset(0.0F, 4.0F, 0.0F));
        head.addOrReplaceChild("helmet_r1", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, -0.1309F, 0.0F, 0.0F));
        PartDefinition upper_body = PartDefinition.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offset(0.0F, 13.0F, 0.0F));
        PartDefinition mortar = upper_body.addOrReplaceChild("mortar", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -6.0F));
        mortar.addOrReplaceChild("launcher_r1", CubeListBuilder.create().texOffs(48, 38).mirror().addBox(-4.0F, -7.0F, -4.0F, 8.0F, 14.0F, 8.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3054F, 0.0F, 0.0F));
        PartDefinition icicle = mortar.addOrReplaceChild("icicle", CubeListBuilder.create().texOffs(80, 49).addBox(-1.5F, -11.0F, 0.0F, 3.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -5.0F, 0.0F, 0.3054F, 0.0F, 0.0F));
        icicle.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(80, 49).addBox(-1.5F, -5.5F, 0.0F, 3.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.5F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition icicle2 = mortar.addOrReplaceChild("icicle2", CubeListBuilder.create().texOffs(80, 49).addBox(-1.5F, -11.0F, 0.0F, 3.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -5.0F, 0.0F, 0.3054F, 0.0F, 0.0F));
        icicle2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(80, 49).addBox(-1.5F, -5.5F, 0.0F, 3.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.5F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition icicle3 = mortar.addOrReplaceChild("icicle3", CubeListBuilder.create().texOffs(80, 49).addBox(-1.5F, -11.0F, 0.0F, 3.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -4.0F, -3.0F, 0.3054F, 0.0F, 0.0F));
        icicle3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(80, 49).addBox(-1.5F, -5.5F, 0.0F, 3.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.5F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition icicle4 = mortar.addOrReplaceChild("icicle4", CubeListBuilder.create().texOffs(80, 49).addBox(-1.5F, -11.0F, 0.0F, 3.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -4.0F, -3.0F, 0.3054F, 0.0F, 0.0F));
        icicle4.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(80, 49).addBox(-1.5F, -5.5F, 0.0F, 3.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.5F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition.addOrReplaceChild("lower_body", CubeListBuilder.create().texOffs(0, 36).mirror().addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(modelData, 128, 64);
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.yRot = headYaw * 0.017453292F;
        this.head.xRot = headPitch * 0.017453292F;
        this.upperBody.yRot = headYaw * 0.017453292F * 0.25F;
        this.head.visible = entity.getHead() == 1;
    }

    public ModelPart root() {
        return this.root;
    }

    public ModelPart getHead() {
        return this.head;
    }
}
