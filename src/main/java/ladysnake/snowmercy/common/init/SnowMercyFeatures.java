package ladysnake.snowmercy.common.init;

import java.util.Map;

import ladysnake.snowmercy.common.SnowMercy;
import ladysnake.snowmercy.common.structure.IcepostFeature;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.Structure.StructureSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class SnowMercyFeatures
{
	public static final ResourceLocation ICEPOST_LOCATION = new ResourceLocation(SnowMercy.MODID, "icepost");
	@SuppressWarnings("deprecation")
	public static final Holder<Structure> ICEPOST = register(ResourceKey.create(Registry.STRUCTURE_REGISTRY, ICEPOST_LOCATION), new IcepostFeature(new StructureSettings(BuiltinRegistries.BIOME.getOrCreateTag(SnowMercyBiomeTagsProvider.PILLAR_BIOMES), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	public static final Holder<StructureSet> ICEPOST_SETS = register(ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, ICEPOST_LOCATION), new StructureSet(ICEPOST, new RandomSpreadStructurePlacement(20, 5, RandomSpreadType.LINEAR, 556312282)));
	
	public static final DeferredRegister<Structure> STRUCTURE = DeferredRegister.create(Registry.STRUCTURE_REGISTRY, SnowMercy.MODID);
	public static final DeferredRegister<StructureSet> STRUCTURE_SET = DeferredRegister.create(Registry.STRUCTURE_SET_REGISTRY, SnowMercy.MODID);
	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, SnowMercy.MODID);
	public static final RegistryObject<Structure> ICEPOST_STRUCTURE = STRUCTURE.register("icepost", () -> ICEPOST.get());
	public static final RegistryObject<StructureSet> ICEPOST_STRUCTURESETS = STRUCTURE_SET.register("icepost", () -> ICEPOST_SETS.get());
	public static final RegistryObject<StructureType<IcepostFeature>> ICEPOST_STRUCTURETYPE = STRUCTURE_TYPE.register("icepost", () -> new SnowMercyStructureType().ICEPOST_TYPE);

	private static Holder<Structure> register(ResourceKey<Structure> p_236534_, Structure p_236535_)
	{
		return BuiltinRegistries.register(BuiltinRegistries.STRUCTURES, p_236534_, p_236535_);
	}
	
	static Holder<StructureSet> register(ResourceKey<StructureSet> p_211129_, StructureSet p_211130_) 
	{
		return BuiltinRegistries.register(BuiltinRegistries.STRUCTURE_SETS, p_211129_, p_211130_);
	}
}
