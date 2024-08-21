package ladysnake.snowmercy.common.init;

import ladysnake.snowmercy.common.SnowMercy;
import ladysnake.snowmercy.common.item.CoalBurnerItem;
import ladysnake.snowmercy.common.item.SkillotineItem;
import ladysnake.snowmercy.common.item.SledgeItem;
import ladysnake.snowmercy.common.item.SnowMercySummonerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SnowMercyItems {
    public static final DeferredRegister<net.minecraft.world.item.Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SnowMercy.MODID);
    
    public static RegistryObject<SkillotineItem> SKILLOTINE = ITEMS.register("skillotine", () -> new SkillotineItem(Tiers.DIAMOND, 3, -2.4f, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
    public static RegistryObject<CoalBurnerItem> COAL_BURNER = ITEMS.register("coal_burner", () -> new CoalBurnerItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1).durability(13000)));
    public static RegistryObject<SledgeItem> SLEDGE = ITEMS.register("hammersledge", () -> new SledgeItem(new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION).stacksTo(1)));
    public static RegistryObject<SnowMercySummonerItem> SUMMONER = ITEMS.register("summoner", () -> new SnowMercySummonerItem(new Item.Properties().stacksTo(1)));
    public static RegistryObject<BlockItem> FROZEN_LODESTONE = ITEMS.register("frozen_lodestone", () -> new BlockItem(SnowMercyBlocks.FROZEN_LODESTONE.get(), new Item.Properties()));
}