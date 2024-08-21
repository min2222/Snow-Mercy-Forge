package ladysnake.snowmercy.common.init;

import java.util.Map;

import ladysnake.snowmercy.common.SnowMercy;
import ladysnake.snowmercy.common.structure.IcepostFeature;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
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
	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE = DeferredRegister.create(Registries.STRUCTURE_TYPE, SnowMercy.MODID);
	public static final ResourceLocation ICEPOST_LOCATION = new ResourceLocation(SnowMercy.MODID, "icepost");
    public static final ResourceKey<Structure> ICEPOST_KEY = ResourceKey.create(Registries.STRUCTURE, ICEPOST_LOCATION);
    public static final ResourceKey<StructureSet> ICEPOST_SET_KEY = ResourceKey.create(Registries.STRUCTURE_SET, ICEPOST_LOCATION);
	public static final TagKey<Biome> PILLAR_BIOMES = TagKey.create(Registries.BIOME, new ResourceLocation(SnowMercy.MODID, "pillar_biomes"));
	
	public static final RegistryObject<StructureType<IcepostFeature>> ICEPOST_STRUCTURETYPE = STRUCTURE_TYPE.register("icepost", () -> new SnowMercyStructureType().ICEPOST_TYPE);
	
	public static void bootstrapStructures(BootstapContext<Structure> context) {
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
		context.register(ICEPOST_KEY, new IcepostFeature(new StructureSettings(biomes.getOrThrow(PILLAR_BIOMES), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	}
    
	public static void bootstrapSets(BootstapContext<StructureSet> context) {
		HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
		context.register(ICEPOST_SET_KEY, new StructureSet(structures.getOrThrow(ICEPOST_KEY), new RandomSpreadStructurePlacement(20, 5, RandomSpreadType.LINEAR, 556312282)));
	}
}
