package ladysnake.snowmercy.client;

import ladysnake.snowmercy.client.render.entity.GiftPackageEntityRenderer;
import ladysnake.snowmercy.client.render.entity.HeadmasterEntityRenderer;
import ladysnake.snowmercy.client.render.entity.IceHeartEntityRenderer;
import ladysnake.snowmercy.client.render.entity.IceballEntityRenderer;
import ladysnake.snowmercy.client.render.entity.IcicleEntityRenderer;
import ladysnake.snowmercy.client.render.entity.InvisibleThrownEntityRenderer;
import ladysnake.snowmercy.client.render.entity.SledgeEntityRenderer;
import ladysnake.snowmercy.client.render.entity.SnowGolemHeadEntityRenderer;
import ladysnake.snowmercy.client.render.entity.WeaponizedSnowGolemEntityRenderer;
import ladysnake.snowmercy.client.render.entity.model.GiftPackageEntityModel;
import ladysnake.snowmercy.client.render.entity.model.IceHeartEntityModel;
import ladysnake.snowmercy.client.render.entity.model.SawmanEntityModel;
import ladysnake.snowmercy.client.render.entity.model.SledgeEntityModel;
import ladysnake.snowmercy.client.render.entity.model.SnowGolemHeadEntityModel;
import ladysnake.snowmercy.client.render.entity.model.WeaponizedSnowGolemEntityModel;
import ladysnake.snowmercy.common.SnowMercy;
import ladysnake.snowmercy.common.entity.ChillSnugglesEntity;
import ladysnake.snowmercy.common.entity.IceboomboxEntity;
import ladysnake.snowmercy.common.entity.MortarsEntity;
import ladysnake.snowmercy.common.entity.RocketsEntity;
import ladysnake.snowmercy.common.entity.SawmanEntity;
import ladysnake.snowmercy.common.entity.SnugglesEntity;
import ladysnake.snowmercy.common.init.SnowMercyEntities;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.FoxRenderer;
import net.minecraft.client.renderer.entity.PolarBearRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SnowMercy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SnowMercyClient 
{
    public static final ModelLayerLocation SNOW_GOLEM_HEAD_MODEL_LAYER = new ModelLayerLocation(SnowMercy.id("snow_golem_head"), "main");
    public static final ModelLayerLocation SLEDGE_MODEL_LAYER = new ModelLayerLocation(SnowMercy.id("sledge"), "main");
    public static final ModelLayerLocation HEART_OF_ICE_MODEL_LAYER = new ModelLayerLocation(SnowMercy.id("heart_of_ice"), "main");
    public static final ModelLayerLocation GIFT_PACKAGE_MODEL_LAYER = new ModelLayerLocation(SnowMercy.id("gift_package"), "main");

    public static final ModelLayerLocation SAWMAN_MODEL_LAYER = new ModelLayerLocation(SnowMercy.id("sawman"), "main");
    public static final ModelLayerLocation ROCKETS_MODEL_LAYER = new ModelLayerLocation(SnowMercy.id("aftermarket_snowman"), "main");
    public static final ModelLayerLocation SNUGGLES_MODEL_LAYER = new ModelLayerLocation(SnowMercy.id("mister_snuggles"), "main");
    public static final ModelLayerLocation MORTARS_MODEL_LAYER = new ModelLayerLocation(SnowMercy.id("ice_mortar"), "main");
    public static final ModelLayerLocation CHILL_SNUGGLES_MODEL_LAYER = new ModelLayerLocation(SnowMercy.id("mister_chill_snuggles"), "main");
    public static final ModelLayerLocation BOOMBOX_MODEL_LAYER = new ModelLayerLocation(SnowMercy.id("iceboombox"), "main");
    
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
    	event.registerLayerDefinition(SAWMAN_MODEL_LAYER, SawmanEntityModel::getTexturedModelData);
        event.registerLayerDefinition(ROCKETS_MODEL_LAYER, WeaponizedSnowGolemEntityModel::rocketsModelData);
        event.registerLayerDefinition(SNUGGLES_MODEL_LAYER, WeaponizedSnowGolemEntityModel::snugglesModelData);
        event.registerLayerDefinition(MORTARS_MODEL_LAYER, WeaponizedSnowGolemEntityModel::mortarsModelData);
        event.registerLayerDefinition(CHILL_SNUGGLES_MODEL_LAYER, WeaponizedSnowGolemEntityModel::snugglesModelData);
        event.registerLayerDefinition(BOOMBOX_MODEL_LAYER, WeaponizedSnowGolemEntityModel::boomboxModelData);
        
        event.registerLayerDefinition(SNOW_GOLEM_HEAD_MODEL_LAYER, SnowGolemHeadEntityModel::getTexturedModelData);
        event.registerLayerDefinition(SLEDGE_MODEL_LAYER, SledgeEntityModel::getTexturedModelData);
        event.registerLayerDefinition(HEART_OF_ICE_MODEL_LAYER, IceHeartEntityModel::getTexturedModelData);
        event.registerLayerDefinition(GIFT_PACKAGE_MODEL_LAYER, GiftPackageEntityModel::getTexturedModelData);
    }
    
    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(SnowMercyEntities.SNOW_GOLEM_HEAD.get(), SnowGolemHeadEntityRenderer::new);
        event.registerEntityRenderer(SnowMercyEntities.HEADMASTER.get(), HeadmasterEntityRenderer::new);
        event.registerEntityRenderer(SnowMercyEntities.ICICLE.get(), IcicleEntityRenderer::new);

        event.registerEntityRenderer(SnowMercyEntities.BURNING_COAL.get(), InvisibleThrownEntityRenderer::new);
        event.registerEntityRenderer(SnowMercyEntities.FREEZING_WIND.get(), InvisibleThrownEntityRenderer::new);
        event.registerEntityRenderer(SnowMercyEntities.SLEDGE.get(), SledgeEntityRenderer::new);
        event.registerEntityRenderer(SnowMercyEntities.HEART_OF_ICE.get(), IceHeartEntityRenderer::new);
        event.registerEntityRenderer(SnowMercyEntities.GIFT_PACKAGE.get(), GiftPackageEntityRenderer::new);

        event.registerEntityRenderer(SnowMercyEntities.POLAR_BEARER.get(), PolarBearRenderer::new);
        event.registerEntityRenderer(SnowMercyEntities.TUNDRABID.get(), FoxRenderer::new);
        event.registerEntityRenderer(SnowMercyEntities.ICEBALL.get(), IceballEntityRenderer::new);
        
        event.registerEntityRenderer(SnowMercyEntities.SAWMAN.get(), ctx -> new WeaponizedSnowGolemEntityRenderer<SawmanEntity, SawmanEntityModel<SawmanEntity>>(ctx, new SawmanEntityModel<>(ctx.bakeLayer(SAWMAN_MODEL_LAYER)), 0.5F, SnowMercy.id("textures/entity/sawman.png")));
        event.registerEntityRenderer(SnowMercyEntities.ROCKETS.get(), ctx -> new WeaponizedSnowGolemEntityRenderer<RocketsEntity, WeaponizedSnowGolemEntityModel<RocketsEntity>>(ctx, new WeaponizedSnowGolemEntityModel<>(ctx.bakeLayer(ROCKETS_MODEL_LAYER)), 0.5F, SnowMercy.id("textures/entity/aftermarket_snowman.png")));
        event.registerEntityRenderer(SnowMercyEntities.SNUGGLES.get(), ctx -> new WeaponizedSnowGolemEntityRenderer<SnugglesEntity, WeaponizedSnowGolemEntityModel<SnugglesEntity>>(ctx, new WeaponizedSnowGolemEntityModel<>(ctx.bakeLayer(SNUGGLES_MODEL_LAYER)), 0.5F, SnowMercy.id("textures/entity/mister_snuggles.png")));
        event.registerEntityRenderer(SnowMercyEntities.MORTARS.get(), ctx -> new WeaponizedSnowGolemEntityRenderer<MortarsEntity, WeaponizedSnowGolemEntityModel<MortarsEntity>>(ctx, new WeaponizedSnowGolemEntityModel<>(ctx.bakeLayer(MORTARS_MODEL_LAYER)), 0.5F, SnowMercy.id("textures/entity/ice_mortar.png")));
        event.registerEntityRenderer(SnowMercyEntities.CHILL_SNUGGLES.get(), ctx -> new WeaponizedSnowGolemEntityRenderer<ChillSnugglesEntity, WeaponizedSnowGolemEntityModel<ChillSnugglesEntity>>(ctx, new WeaponizedSnowGolemEntityModel<>(ctx.bakeLayer(CHILL_SNUGGLES_MODEL_LAYER)), 0.5F, SnowMercy.id("textures/entity/mister_chill_snuggles.png")));
        event.registerEntityRenderer(SnowMercyEntities.BOOMBOX.get(), ctx -> new WeaponizedSnowGolemEntityRenderer<IceboomboxEntity, WeaponizedSnowGolemEntityModel<IceboomboxEntity>>(ctx, new WeaponizedSnowGolemEntityModel<>(ctx.bakeLayer(BOOMBOX_MODEL_LAYER)), 0.5F, SnowMercy.id("textures/entity/iceboombox.png")));
    }
}
