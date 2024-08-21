package ladysnake.snowmercy.common.init;

import ladysnake.snowmercy.common.SnowMercy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SnowMercySoundEvents {

	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SnowMercy.MODID);

	public static RegistryObject<SoundEvent> JINGLE_BELLS = registerSound("music_disc.jingle_bells");
	public static RegistryObject<SoundEvent> HEART_OF_ICE_AMBIENT = registerSound("heart_of_ice_ambient");
	
	private static RegistryObject<SoundEvent> registerSound(String name) 
	{
		return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(SnowMercy.MODID, name)));
    }
}