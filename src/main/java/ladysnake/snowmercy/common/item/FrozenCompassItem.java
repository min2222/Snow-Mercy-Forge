package ladysnake.snowmercy.common.item;

import java.util.List;

import ladysnake.snowmercy.common.init.SnowMercyBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class FrozenCompassItem extends Item {
    public FrozenCompassItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().getBlockState(context.getClickedPos()).getBlock() == Blocks.LODESTONE) {
        	//TODO
            /*if (SnowMercyComponents.SNOWMERCY.get(context.getLevel()).isEventOngoing()) {
                if (context.getPlayer() != null) {
                    context.getPlayer().displayClientMessage(Component.translatable("info.snowmercy.ongoing"), true);
                }
                return InteractionResult.FAIL;
            } else */{
                context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);

                context.getLevel().setBlockAndUpdate(context.getClickedPos(), Blocks.BLUE_ICE.defaultBlockState());
                context.getLevel().destroyBlock(context.getClickedPos(), false);
                context.getLevel().setBlockAndUpdate(context.getClickedPos(), Blocks.SNOW_BLOCK.defaultBlockState());
                context.getLevel().destroyBlock(context.getClickedPos(), false);
                context.getLevel().setBlockAndUpdate(context.getClickedPos(), SnowMercyBlocks.FROZEN_LODESTONE.get().defaultBlockState());
                if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
                    context.getItemInHand().shrink(1);
                }

                if (context.getLevel() instanceof ServerLevel) {
                	//TODO
                    //SnowMercyComponents.SNOWMERCY.get(context.getLevel()).startEvent(context.getLevel());
                }

                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("item.snowmercy.frozen_compass.tooltip").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));
    }
}
