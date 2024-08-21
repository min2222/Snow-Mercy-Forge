package ladysnake.snowmercy.client.render.entity.model;// Made with Blockbench 4.0.5
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports


import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import ladysnake.snowmercy.common.entity.IceHeartEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class IceHeartEntityModel<T extends Entity> extends EntityModel<T> {
    private final ModelPart heart;
    private float yaw = 0;
    private float pitch = 0;
    private float roll = 0;


    public IceHeartEntityModel(ModelPart root) {
        this.heart = root.getChild("heart");
    }

    public static LayerDefinition getTexturedModelData() {
    	MeshDefinition modelData = new MeshDefinition();
    	PartDefinition modelPartData = modelData.getRoot();

    	modelPartData.addOrReplaceChild("heart", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, 4.0F, 0.0F));
	
        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if (entity instanceof IceHeartEntity && ((IceHeartEntity) entity).isActive()) {
            this.heart.yRot = yaw += new Random().nextFloat() / 20f;
            this.heart.xRot = pitch += new Random().nextFloat() / 20f;
            this.heart.zRot = roll += new Random().nextFloat() / 20f;
        } else {
            this.heart.yRot = yaw += 0.001f;
            this.heart.xRot = pitch += 0.001f;
            this.heart.zRot = roll += 0.001f;
        }
//        this.heart.setPivot(0f, (float) (89f+Math.sin(entity.age/50f)*10f), 0f);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        heart.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}