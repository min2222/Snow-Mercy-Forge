package ladysnake.snowmercy.common;

import ladysnake.snowmercy.common.entity.WeaponizedGolemType;
import ladysnake.snowmercy.common.init.SnowMercyBlocks;
import ladysnake.snowmercy.common.init.SnowMercyEntities;
import ladysnake.snowmercy.common.init.SnowMercyFeatures;
import ladysnake.snowmercy.common.init.SnowMercyItems;
import ladysnake.snowmercy.common.init.SnowMercySoundEvents;
import ladysnake.snowmercy.common.init.SnowMercyWaves;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(SnowMercy.MODID)
public class SnowMercy {
    public static final String MODID = "snowmercy";
    //private static final RandomSpawnCollection<EntityType<? extends LivingEntity>> SPAWN_CANDIDATES = new RandomSpawnCollection<>();

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }
    
    public SnowMercy() {
    	IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    	bus.addListener(this::CompleteSetup);
        GeckoLib.initialize();

        SnowMercyBlocks.BLOCKS.register(bus);
        SnowMercyEntities.ENTITY_TYPES.register(bus);
        SnowMercySoundEvents.SOUND_EVENTS.register(bus);
        SnowMercyItems.ITEMS.register(bus);
        SnowMercyFeatures.STRUCTURE.register(bus);
        SnowMercyFeatures.STRUCTURE_SET.register(bus);
        SnowMercyFeatures.STRUCTURE_TYPE.register(bus);
        EntityDataSerializers.registerSerializer(WeaponizedGolemType.TRACKED_DATA_HANDLER);	
    }
    
	private void CompleteSetup(FMLLoadCompleteEvent event)
    {
        SnowMercyWaves.init();
    }
}
