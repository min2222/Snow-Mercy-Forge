package ladysnake.snowmercy.common;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import ladysnake.snowmercy.common.entity.WeaponizedGolemType;
import ladysnake.snowmercy.common.init.SnowMercyBlocks;
import ladysnake.snowmercy.common.init.SnowMercyEntities;
import ladysnake.snowmercy.common.init.SnowMercyFeatures;
import ladysnake.snowmercy.common.init.SnowMercyItems;
import ladysnake.snowmercy.common.init.SnowMercySoundEvents;
import ladysnake.snowmercy.common.init.SnowMercyWaves;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;

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
        SnowMercyFeatures.STRUCTURE_TYPE.register(bus);
        bus.addListener(this::gatherData);
        EntityDataSerializers.registerSerializer(WeaponizedGolemType.TRACKED_DATA_HANDLER);	
    }
    
	private void CompleteSetup(FMLLoadCompleteEvent event)
    {
        SnowMercyWaves.init();
    }
	
    private void gatherData(final GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        dataGenerator.addProvider(event.includeServer(), SnowMercyWorldGenerator.addProviders(dataGenerator.getPackOutput(), event.getLookupProvider()));
    }
    
    public static class SnowMercyWorldGenerator extends DatapackBuiltinEntriesProvider {
        public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
    			.add(Registries.STRUCTURE, SnowMercyFeatures::bootstrapStructures)
    			.add(Registries.STRUCTURE_SET, SnowMercyFeatures::bootstrapSets);
        
    	private SnowMercyWorldGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
    		super(output, provider, BUILDER, Set.of(SnowMercy.MODID));
    	}

    	public static DataProvider addProviders(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
    		DataProvider data = new SnowMercyWorldGenerator(output, provider);
    		return data;
    	}
    }
}
