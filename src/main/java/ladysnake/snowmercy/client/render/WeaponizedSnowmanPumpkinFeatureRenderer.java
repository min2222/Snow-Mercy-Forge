package ladysnake.snowmercy.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import ladysnake.snowmercy.client.render.entity.model.WeaponizedSnowGolemEntityModel;
import ladysnake.snowmercy.common.entity.WeaponizedSnowGolemEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class WeaponizedSnowmanPumpkinFeatureRenderer<E extends WeaponizedSnowGolemEntity, M extends WeaponizedSnowGolemEntityModel<E>> extends RenderLayer<E, M> {
    public WeaponizedSnowmanPumpkinFeatureRenderer(RenderLayerParent<E, M> featureRendererContext) {
        super(featureRendererContext);
    }

    public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, E snowGolemEntity, float f, float g, float h, float j, float k, float l) {
        if (snowGolemEntity.getHead() == 2) {
            Minecraft minecraftClient = Minecraft.getInstance();
            boolean bl = minecraftClient.shouldEntityAppearGlowing(snowGolemEntity) && snowGolemEntity.isInvisible();
            if (!snowGolemEntity.isInvisible() || bl) {
                matrixStack.pushPose();
                this.getParentModel().getHead().translateAndRotate(matrixStack);
                matrixStack.translate(0.0D, -0.34375D, 0.0D);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                matrixStack.scale(0.625F, -0.625F, -0.625F);
                ItemStack itemStack = new ItemStack(Blocks.CARVED_PUMPKIN);
                if (bl) {
                    BlockState blockState = Blocks.CARVED_PUMPKIN.defaultBlockState();
                    BlockRenderDispatcher blockRenderManager = minecraftClient.getBlockRenderer();
                    BakedModel bakedModel = blockRenderManager.getBlockModel(blockState);
                    int n = LivingEntityRenderer.getOverlayCoords(snowGolemEntity, 0.0F);
                    matrixStack.translate(-0.5D, -0.5D, -0.5D);
                    blockRenderManager.getModelRenderer().renderModel(matrixStack.last(), vertexConsumerProvider.getBuffer(RenderType.outline(InventoryMenu.BLOCK_ATLAS)), blockState, bakedModel, 0.0F, 0.0F, 0.0F, i, n, net.minecraftforge.client.model.data.ModelData.EMPTY, null);
                } else {
                    minecraftClient.getItemRenderer().renderStatic(snowGolemEntity, itemStack, ItemTransforms.TransformType.HEAD, false, matrixStack, vertexConsumerProvider, snowGolemEntity.level, i, LivingEntityRenderer.getOverlayCoords(snowGolemEntity, 0.0F), snowGolemEntity.getId());
                }

                matrixStack.popPose();
            }
        }
    }
}

