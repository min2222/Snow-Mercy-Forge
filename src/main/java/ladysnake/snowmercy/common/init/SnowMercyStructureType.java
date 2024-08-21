package ladysnake.snowmercy.common.init;

import com.mojang.serialization.Codec;

import ladysnake.snowmercy.common.structure.IcepostFeature;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class SnowMercyStructureType implements StructureType<IcepostFeature>
{
	StructureType<IcepostFeature> ICEPOST_TYPE = register("snowmercy:icepost", IcepostFeature.CODEC);
	
	private static <S extends Structure> StructureType<S> register(String p_226882_, Codec<S> p_226883_) {
		return Registry.register(Registry.STRUCTURE_TYPES, p_226882_, () -> {
			return p_226883_;
		});
	}
	   
	@Override
	public Codec<IcepostFeature> codec()
	{
		return IcepostFeature.CODEC;
	}
}
