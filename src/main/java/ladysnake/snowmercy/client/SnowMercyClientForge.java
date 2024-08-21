package ladysnake.snowmercy.client;

import ladysnake.snowmercy.client.sound.BoomboxMovingSoundInstance;
import ladysnake.snowmercy.common.SnowMercy;
import ladysnake.snowmercy.common.entity.IceboomboxEntity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SnowMercy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class SnowMercyClientForge 
{
    @SubscribeEvent
    public static void entityJoin(EntityJoinLevelEvent evt)
    {
    	if(evt.getEntity() instanceof IceboomboxEntity)
    	{
    		Minecraft.getInstance().getSoundManager().queueTickingSound(new BoomboxMovingSoundInstance((IceboomboxEntity) evt.getEntity()));
    	}
    }
}
