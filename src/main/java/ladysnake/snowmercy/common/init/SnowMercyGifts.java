package ladysnake.snowmercy.common.init;

import java.util.Random;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public class SnowMercyGifts {
    public static ItemStack getRandomGift() {
        Random random = new Random();

        ItemStack ret = ItemStack.EMPTY;

        switch (random.nextInt(4)) {
            // food
            case 0 -> {
                switch (random.nextInt(5)) {
                    case 0 -> ret = new ItemStack(Items.COOKED_CHICKEN, 4 + random.nextInt(5));
                    case 1 -> ret = new ItemStack(Items.CAKE, 1);
                    case 2 -> ret = new ItemStack(Items.COOKIE, 16 + random.nextInt(17));
                    case 3 -> ret = new ItemStack(Items.RABBIT_STEW, 1);
                    case 4 -> ret = new ItemStack(Items.GOLDEN_APPLE, 1);
                }
            }
            // pets
            case 1 -> {
                switch (random.nextInt(5)) {
                    case 0 -> ret = new ItemStack(Items.AXOLOTL_SPAWN_EGG, 1);
                    case 1 -> ret = new ItemStack(Items.WOLF_SPAWN_EGG, 1);
                    case 2 -> ret = new ItemStack(Items.CAT_SPAWN_EGG, 1);
                    case 3 -> ret = new ItemStack(Items.PARROT_SPAWN_EGG, 1);
                    case 4 -> ret = new ItemStack(Items.RABBIT_SPAWN_EGG, 1);
                }
            }
            // unique items
            case 2 -> {
                switch (random.nextInt(5)) {
                    case 0 -> ret = new ItemStack(SnowMercyItems.SKILLOTINE.get(), 1);
                    case 1 -> ret = new ItemStack(SnowMercyItems.COAL_BURNER.get(), 1);
                    case 2 -> ret = new ItemStack(SnowMercyItems.SLEDGE.get(), 1);
                    case 3 -> {
                        ret = new ItemStack(Items.CROSSBOW, 1);
                        ret.enchant(Enchantments.QUICK_CHARGE, 3);
                        ret.enchant(Enchantments.MULTISHOT, 1);
                        ret.setHoverName(Component.translatable("item.snowmercy.firework_launcher"));
                    }
                    case 4 -> {
                        ret = new ItemStack(Items.LEATHER_BOOTS, 1);
                        ret.enchant(Enchantments.FROST_WALKER, 3);
                        ret.setHoverName(Component.translatable("item.snowmercy.bootleg_ice_skaters"));
                    }
                }
            }
            // valuables
            case 3 -> {
                switch (random.nextInt(7)) {
                    case 0 -> ret = new ItemStack(Items.DIAMOND, 1);
                    case 1 -> ret = new ItemStack(Items.EMERALD, 4 + random.nextInt(5));
                    case 2 -> ret = new ItemStack(Items.IRON_INGOT, 8 + random.nextInt(9));
                    case 3 -> ret = new ItemStack(Items.COAL, 16 + random.nextInt(17));
                    case 4 -> ret = new ItemStack(Items.ENDER_PEARL, 2 + random.nextInt(3));
                    case 5 -> ret = new ItemStack(Items.GOLDEN_APPLE, 1);
                    case 6 -> ret = new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1);
                }
            }
        }

        return ret;
    }
}
