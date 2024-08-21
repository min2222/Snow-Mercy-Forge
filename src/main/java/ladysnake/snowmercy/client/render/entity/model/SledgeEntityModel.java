package ladysnake.snowmercy.client.render.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import ladysnake.snowmercy.common.entity.SledgeEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SledgeEntityModel extends HierarchicalModel<SledgeEntity> {
    private final ModelPart sledge;

    public SledgeEntityModel(ModelPart root) {
        this.sledge = root.getChild("sledge");
    }

    public static LayerDefinition getTexturedModelData() {
    	MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition sledge = modelPartData.addOrReplaceChild("sledge", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, 0.0F, -6.0F, 12.0F, 4.0F, 23.0F)
                .texOffs(0, 27).addBox(-10.0F, -1.001F, -2.0F, 12.0F, 1.0F, 19.0F)
                .texOffs(0, 58).addBox(-13.001F, 0.0F, -1.0F, 3.0F, 3.0F, 12.0F)
                .texOffs(0, 58).addBox(2.001F, 0.0F, -1.0F, 3.0F, 3.0F, 12.0F)
                .texOffs(22, 63).addBox(3.5F, 0.0F, 11.0F, 0.0F, 3.0F, 4.0F)
                .texOffs(22, 63).addBox(-11.5F, 0.0F, 11.0F, 0.0F, 3.0F, 4.0F), PartPose.offset(4.0F, 1.90F, -6.0F));

        sledge.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 47).addBox(-7.0F, -5.0F, 0.0F, 14.0F, 5.0F, 6.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-4.0F, 4.0F, -6.0F, 0.7418F, 0.0F, 0.0F));

        return LayerDefinition.create(modelData, 128, 128);
    }

    @Override
    public void setupAnim(SledgeEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.sledge.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.sledge;
    }
}

