package ladysnake.snowmercy.common;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class SnowMercyTags {
	public static final TagKey<EntityType<?>> SNOWMAN = createEntityTypeTagKey("snowman");
	
	private static TagKey<EntityType<?>> createEntityTypeTagKey(String p_203849_) 
	{
		return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(SnowMercy.MODID, p_203849_));
	}
}
