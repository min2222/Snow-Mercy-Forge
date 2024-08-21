package ladysnake.snowmercy.common.item;

import java.util.List;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableSet;

import ladysnake.snowmercy.common.entity.BurningCoalEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;

public class CoalBurnerItem extends ProjectileWeaponItem implements Vanishable {

    public CoalBurnerItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return ARROW_OR_FIREWORK;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 4;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if ((user.isCreative() || user.getInventory().hasAnyOf(ImmutableSet.of(Items.COAL, Items.CHARCOAL, Items.BLAZE_POWDER))) && !user.isUnderWater()) {
            user.startUsingItem(hand);
            user.playSound(SoundEvents.FIRECHARGE_USE, 1.0f, 0.5f);
            user.playSound(SoundEvents.FIRE_AMBIENT, 1.0f, 1.0f);
            return InteractionResultHolder.consume(itemStack);
        }
        user.playSound(SoundEvents.LEVER_CLICK, 1.0f, 0.8f);
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public void onUseTick(Level world, LivingEntity entity, ItemStack stack, int remainingUseTicks) {
        if (entity instanceof Player) {
        	Player user = (Player) entity;
            if ((user.isCreative() || user.getInventory().hasAnyOf(ImmutableSet.of(Items.COAL, Items.CHARCOAL, Items.BLAZE_POWDER))) && !user.isUnderWater()) {
                boolean extraHot = false;

                stack.hurtAndBreak(1, user, p -> p.broadcastBreakEvent(user.getMainHandItem().equals(stack) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND));

                for (int i = 0; i < user.getInventory().getContainerSize(); i++) {
                    ItemStack itemStack = user.getInventory().getItem(i);
                    if (itemStack.getItem() == Items.COAL || itemStack.getItem() == Items.CHARCOAL) {
                        if (!user.isCreative() && (remainingUseTicks % 40 == 0 || remainingUseTicks == this.getUseDuration(stack))) {
                            itemStack.shrink(1);
                        }
                        break;
                    } else if (itemStack.getItem() == Items.BLAZE_POWDER) {
                        if (!user.isCreative()) {
                            itemStack.shrink(1);
                        }
                        extraHot = true;
                        break;
                    }
                }

                if (!world.isClientSide) {
                    BurningCoalEntity burningCoalEntity = new BurningCoalEntity(world, user);
                    burningCoalEntity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0f, 1.5f, 1.0f);
                    burningCoalEntity.setExtraHot(extraHot);
                    world.addFreshEntity(burningCoalEntity);
                }

                if (remainingUseTicks % 5 == 0) {
                    user.playSound(SoundEvents.FIRECHARGE_USE, 1.0f, 0.5f);
                    user.playSound(SoundEvents.FIRE_AMBIENT, 1.0f, 1.0f);
                }
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 999999990;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("item.snowmercy.coal_burner.tooltip1").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));
            tooltip.add(Component.translatable("item.snowmercy.coal_burner.tooltip2").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA).applyFormat(ChatFormatting.ITALIC)));
            tooltip.add(Component.translatable("item.snowmercy.coal_burner.tooltip3").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA).applyFormat(ChatFormatting.ITALIC)));
        } else {
            tooltip.add(Component.translatable("tip.snowmercy.sneak_tooltip").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }
    }
}

