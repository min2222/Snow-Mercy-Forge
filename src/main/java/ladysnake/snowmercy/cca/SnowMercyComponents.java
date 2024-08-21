package ladysnake.snowmercy.cca;

public final class SnowMercyComponents 
{
	//TODO
	/*implements WorldComponentInitializer {
    public static final ComponentKey<SnowMercyEventComponent> SNOWMERCY =
            ComponentRegistryV3.INSTANCE.getOrCreate(new ResourceLocation(SnowMercy.MODID, "snowmercy"), SnowMercyEventComponent.class);

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        // Add the component to every World instance
        registry.register(SNOWMERCY, world -> new SnowMercyEventComponent());
    }*/
	
	public static final SnowMercyEventComponent SNOWMERCY = new SnowMercyEventComponent();	
}