package ladysnake.snowmercy.common;

import ladysnake.snowmercy.common.entity.HeadmasterEntity;
import ladysnake.snowmercy.common.entity.IceballEntity;
import ladysnake.snowmercy.common.entity.IceboomboxEntity;
import ladysnake.snowmercy.common.entity.MortarsEntity;
import ladysnake.snowmercy.common.entity.PolarBearerEntity;
import ladysnake.snowmercy.common.entity.RocketsEntity;
import ladysnake.snowmercy.common.entity.SawmanEntity;
import ladysnake.snowmercy.common.entity.SnowGolemHeadEntity;
import ladysnake.snowmercy.common.entity.SnugglesEntity;
import ladysnake.snowmercy.common.entity.TundrabidEntity;
import ladysnake.snowmercy.common.init.SnowMercyEntities;
import ladysnake.snowmercy.common.init.SnowMercyItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SnowMercy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SnowMercyCommon 
{
    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) 
    {
        event.put(SnowMercyEntities.SNUGGLES.get(), SnugglesEntity.createEntityAttributes().build());
        event.put(SnowMercyEntities.CHILL_SNUGGLES.get(), SnugglesEntity.createEntityAttributes().build());
        event.put(SnowMercyEntities.ROCKETS.get(), RocketsEntity.createEntityAttributes().build());
        event.put(SnowMercyEntities.MORTARS.get(), MortarsEntity.createEntityAttributes().build());
        event.put(SnowMercyEntities.SAWMAN.get(), SawmanEntity.createEntityAttributes().build());
        event.put(SnowMercyEntities.BOOMBOX.get(), IceboomboxEntity.createEntityAttributes().build());
        event.put(SnowMercyEntities.SNOW_GOLEM_HEAD.get(), SnowGolemHeadEntity.createEntityAttributes().build());
        event.put(SnowMercyEntities.HEADMASTER.get(), HeadmasterEntity.createHeadmasterAttributes().build());
        event.put(SnowMercyEntities.POLAR_BEARER.get(), PolarBearerEntity.createPolarBearerAttributes().build());
        event.put(SnowMercyEntities.TUNDRABID.get(), TundrabidEntity.createTundrabidAttributes().build());
        event.put(SnowMercyEntities.ICEBALL.get(), IceballEntity.createIceballAttributes().build());
    }
    
    @SubscribeEvent
    public static void creativeTabBuild(BuildCreativeModeTabContentsEvent event)
    {
    	if(event.getTabKey() == CreativeModeTabs.COMBAT)
    	{
    		event.accept(SnowMercyItems.SKILLOTINE.get());
    		event.accept(SnowMercyItems.COAL_BURNER.get());
    	}
    	else if(event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS)
    	{
    		event.accept(SnowMercyItems.SLEDGE.get());
    	}
    }
}
