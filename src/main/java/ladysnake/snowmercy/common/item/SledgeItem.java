package ladysnake.snowmercy.common.item;

import java.util.List;
import java.util.function.Predicate;

import ladysnake.snowmercy.common.entity.SledgeEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SledgeItem extends Item {
    private static final Predicate<Entity> RIDERS = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

    public SledgeItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        Object vec3d2;
        ItemStack itemStack = user.getItemInHand(hand);
        BlockHitResult hitResult = SledgeItem.getPlayerPOVHitResult(world, user, ClipContext.Fluid.ANY);
        if (((HitResult) hitResult).getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStack);
        }
        Vec3 vec3d = user.getViewVector(1.0f);
        //double d = 5.0;
        List<Entity> list = world.getEntities(user, user.getBoundingBox().expandTowards(vec3d.scale(5.0)).inflate(1.0), RIDERS);
        if (!list.isEmpty()) {
            vec3d2 = user.getEyePosition();
            for (Entity entity : list) {
                AABB box = entity.getBoundingBox().inflate(entity.getPickRadius());
                if (!box.contains((Vec3) vec3d2)) continue;
                return InteractionResultHolder.pass(itemStack);
            }
        }
        if (((HitResult) hitResult).getType() == HitResult.Type.BLOCK) {
            vec3d2 = new SledgeEntity(world, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z);
            ((Entity) vec3d2).setYRot(user.getYRot());
            if (!world.noCollision((Entity) vec3d2, ((Entity) vec3d2).getBoundingBox())) {
                return InteractionResultHolder.fail(itemStack);
            }
            if (!world.isClientSide) {
                world.addFreshEntity((Entity) vec3d2);
                world.gameEvent((Entity) user, GameEvent.ENTITY_PLACE, BlockPos.containing(hitResult.getLocation()));
                if (!user.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
            }
            user.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("item.snowmercy.hammersledge.tooltip").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));
        } else {
            tooltip.add(Component.translatable("tip.snowmercy.sneak_tooltip").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }
    }
}

