package ladysnake.snowmercy.common.init;

import org.jetbrains.annotations.Nullable;

import ladysnake.snowmercy.common.SnowMercy;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SnowMercyBiomeTagsProvider extends BiomeTagsProvider
{
	public static final TagKey<Biome> PILLAR_BIOMES = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(SnowMercy.MODID, "pillar_biomes"));
	
	public SnowMercyBiomeTagsProvider(DataGenerator p_211094_, String modId, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(p_211094_, modId, existingFileHelper);
	}
	
	@Override
	protected void addTags() 
	{
		this.tag(PILLAR_BIOMES).add(Biomes.SNOWY_PLAINS).add(Biomes.ICE_SPIKES).add(Biomes.SNOWY_TAIGA).add(Biomes.GROVE).add(Biomes.SNOWY_SLOPES).add(Biomes.FROZEN_PEAKS);
	}
}
