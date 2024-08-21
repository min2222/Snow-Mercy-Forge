package ladysnake.snowmercy.common.init;

import ladysnake.snowmercy.common.SnowMercy;
import ladysnake.snowmercy.common.entity.BurningCoalEntity;
import ladysnake.snowmercy.common.entity.ChillSnugglesEntity;
import ladysnake.snowmercy.common.entity.FreezingWindEntity;
import ladysnake.snowmercy.common.entity.GiftPackageEntity;
import ladysnake.snowmercy.common.entity.HeadmasterEntity;
import ladysnake.snowmercy.common.entity.IceHeartEntity;
import ladysnake.snowmercy.common.entity.IceballEntity;
import ladysnake.snowmercy.common.entity.IceboomboxEntity;
import ladysnake.snowmercy.common.entity.IcicleEntity;
import ladysnake.snowmercy.common.entity.MortarsEntity;
import ladysnake.snowmercy.common.entity.PolarBearerEntity;
import ladysnake.snowmercy.common.entity.RocketsEntity;
import ladysnake.snowmercy.common.entity.SawmanEntity;
import ladysnake.snowmercy.common.entity.SledgeEntity;
import ladysnake.snowmercy.common.entity.SnowGolemHeadEntity;
import ladysnake.snowmercy.common.entity.SnugglesEntity;
import ladysnake.snowmercy.common.entity.TundrabidEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SnowMercyEntities {
    
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SnowMercy.MODID);
    
    public static RegistryObject<EntityType<PolarBearerEntity>> POLAR_BEARER;
    public static RegistryObject<EntityType<TundrabidEntity>> TUNDRABID;
    public static RegistryObject<EntityType<IceballEntity>> ICEBALL;

    public static RegistryObject<EntityType<SnugglesEntity>> SNUGGLES;
    public static RegistryObject<EntityType<ChillSnugglesEntity>> CHILL_SNUGGLES;
    public static RegistryObject<EntityType<RocketsEntity>> ROCKETS;
    public static RegistryObject<EntityType<MortarsEntity>> MORTARS;
    public static RegistryObject<EntityType<SawmanEntity>> SAWMAN;
    public static RegistryObject<EntityType<IceboomboxEntity>> BOOMBOX;
    public static RegistryObject<EntityType<SnowGolemHeadEntity>> SNOW_GOLEM_HEAD;
    public static RegistryObject<EntityType<HeadmasterEntity>> HEADMASTER;

    public static RegistryObject<EntityType<IcicleEntity>> ICICLE;
    public static RegistryObject<EntityType<BurningCoalEntity>> BURNING_COAL;
    public static RegistryObject<EntityType<FreezingWindEntity>> FREEZING_WIND;
    public static RegistryObject<EntityType<SledgeEntity>> SLEDGE;
    public static RegistryObject<EntityType<IceHeartEntity>> HEART_OF_ICE;
    public static RegistryObject<EntityType<GiftPackageEntity>> GIFT_PACKAGE;

    static
    {
        POLAR_BEARER = register("polar_bearer", EntityType.Builder.of(PolarBearerEntity::new, MobCategory.MONSTER).immuneTo(Blocks.POWDER_SNOW).sized(1.4f, 1.4f));
        TUNDRABID = register("tundrabid", EntityType.Builder.of(TundrabidEntity::new, MobCategory.MONSTER).sized(0.6F, 0.7F));
        
        ICEBALL = register("iceball", EntityType.Builder.of(IceballEntity::new, MobCategory.MONSTER).sized(2.04f, 2.04f));

        SNUGGLES = register("mister_snuggles", EntityType.Builder.of(SnugglesEntity::new, MobCategory.MONSTER).sized(0.7F, 1.9F));
        CHILL_SNUGGLES = register("mister_chill_snuggles", EntityType.Builder.of(ChillSnugglesEntity::new, MobCategory.MONSTER).sized(0.7F, 1.9F));
        ROCKETS = register("aftermarket_snowman", EntityType.Builder.of(RocketsEntity::new, MobCategory.MONSTER).sized(0.7F, 1.9F));
        MORTARS = register("ice_mortar", EntityType.Builder.of(MortarsEntity::new, MobCategory.MONSTER).sized(0.7F, 1.9F));
        SAWMAN = register("sawman", EntityType.Builder.of(SawmanEntity::new, MobCategory.MONSTER).sized(0.7F, 1.9F));
        BOOMBOX = register("iceboombox", EntityType.Builder.of(IceboomboxEntity::new, MobCategory.MONSTER).sized(0.7F, 1.9F));

        SNOW_GOLEM_HEAD = register("snow_golem_head", EntityType.Builder.<SnowGolemHeadEntity>of(SnowGolemHeadEntity::new, MobCategory.MONSTER).sized(0.5f, 0.6f));
        HEADMASTER = register("headmaster", EntityType.Builder.of(HeadmasterEntity::new, MobCategory.MONSTER).sized(2.0f, 4.0f));

        ICICLE = register("icicle", EntityType.Builder.<IcicleEntity>of(IcicleEntity::new, MobCategory.MISC).sized(0.5f, 0.5f));
        BURNING_COAL = register("burning_coal", EntityType.Builder.<BurningCoalEntity>of(BurningCoalEntity::new, MobCategory.MISC).sized(1.0f, 1.0f));
        FREEZING_WIND = register("freezing_wind", EntityType.Builder.<FreezingWindEntity>of(FreezingWindEntity::new, MobCategory.MISC).sized(1.0f, 1.0f));
        SLEDGE = register("hammersledge", EntityType.Builder.<SledgeEntity>of(SledgeEntity::new, MobCategory.MISC).sized(1.375f, 0.5625f));
        HEART_OF_ICE = register("heart_of_ice", EntityType.Builder.of(IceHeartEntity::new, MobCategory.MISC).sized(0.5f, 0.5f));
        GIFT_PACKAGE = register("gift_package", EntityType.Builder.of(GiftPackageEntity::new, MobCategory.MISC).sized(0.6f, 0.6f));
    }
    
	private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, Builder<T> builder) 
	{
		return ENTITY_TYPES.register(name, () -> builder.build(new ResourceLocation(SnowMercy.MODID, "name").toString()));
    }
}
