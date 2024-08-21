package ladysnake.snowmercy.common.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class SkillotineItem extends ShovelItem {
    public SkillotineItem(Tier material, float attackDamage, float attackSpeed, Item.Properties settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("item.snowmercy.skillotine.tooltip").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));
        } else {
            tooltip.add(Component.translatable("tip.snowmercy.sneak_tooltip").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }
    }
}
