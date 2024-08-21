package ladysnake.snowmercy.common.item;

import java.util.ArrayList;
import java.util.Random;

import ladysnake.snowmercy.common.init.SnowMercyEntities;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;

public class SnowMercySummonerItem extends Item {

    public SnowMercySummonerItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ArrayList<EntityType<? extends Mob>> livingEntities = new ArrayList<>();
        livingEntities.add(SnowMercyEntities.TUNDRABID.get());
        livingEntities.add(SnowMercyEntities.ICEBALL.get());
        livingEntities.add(SnowMercyEntities.SNUGGLES.get());
        livingEntities.add(SnowMercyEntities.CHILL_SNUGGLES.get());
        livingEntities.add(SnowMercyEntities.ROCKETS.get());
        livingEntities.add(SnowMercyEntities.MORTARS.get());
        livingEntities.add(SnowMercyEntities.SAWMAN.get());
        livingEntities.add(SnowMercyEntities.BOOMBOX.get());

        Mob mobEntity = livingEntities.get(new Random().nextInt(livingEntities.size())).create(world);
        mobEntity.setPos(user.getX(), user.getY(), user.getZ());

        float f = MathHelper.wrapDegrees(user.getYRot());
        float g = MathHelper.wrapDegrees(user.getXRot());
        float chunkPos = Mth.clamp(g, -90.0f, 90.0f);
        mobEntity.moveTo(user.getX(), user.getY(), user.getZ(), f, chunkPos);
        mobEntity.setYHeadRot(f);
        mobEntity.setNoAi(true);

        world.addFreshEntity(mobEntity);

        return InteractionResultHolder.success(user.getItemInHand(hand));
    }
}
