package ladysnake.snowmercy.client.render.entity.model;// Made with Blockbench 4.0.5
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import ladysnake.snowmercy.common.entity.GiftPackageEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class GiftPackageEntityModel<T extends Entity> extends EntityModel<T> {
    private final ModelPart gift;

    public GiftPackageEntityModel(ModelPart root) {
        this.gift = root.getChild("gift");
    }

    public static LayerDefinition getTexturedModelData() {
    	MeshDefinition modelData = new MeshDefinition();
    	PartDefinition modelPartData = modelData.getRoot();

    	PartDefinition gift = modelPartData.addOrReplaceChild("gift", CubeListBuilder.create(), PartPose.offset(0.0F, 11F, 0.0F));

    	gift.addOrReplaceChild("box", CubeListBuilder.create().texOffs(0, 60).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F), PartPose.offset(0.0F, 0.0F, 0.0F));

    	PartDefinition tie = gift.addOrReplaceChild("tie", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

    	tie.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(30, 65).addBox(0.0F, -2.0F, -1.001F, 4.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, 0.0F, -0.3054F));

    	tie.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(30, 65).addBox(-4.0F, -2.0F, -0.999F, 4.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.3054F));

    	PartDefinition parachute = gift.addOrReplaceChild("parachute", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -55.0F, -16.0F, 32.0F, 28.0F, 32.0F), PartPose.offset(0.0F, 0.0F, 0.0F));

    	PartDefinition ropes = parachute.addOrReplaceChild("ropes", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

    	ropes.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 111).addBox(-23.0F, -27.0F, 0.0F, 46.0F, 17.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

    	ropes.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 111).addBox(-23.0F, -27.0F, 0.0F, 46.0F, 17.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        return LayerDefinition.create(modelData, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if (((GiftPackageEntity) entity).hasParachute()) {
            gift.getChild("parachute").visible = true;
        } else {
            gift.getChild("parachute").visible = false;
        }

        gift.yRot = entity.getYRot();
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        gift.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}