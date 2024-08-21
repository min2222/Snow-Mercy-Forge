package ladysnake.snowmercy.common.entity;

import java.util.Iterator;

import ladysnake.snowmercy.common.entity.ai.goal.FollowAndBlowGoal;
import ladysnake.snowmercy.common.world.PuffExplosion;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class SnugglesEntity extends WeaponizedSnowGolemEntity {
    public static final ItemStack TNT = new ItemStack(Items.TNT, 1);

    public SnugglesEntity(EntityType<? extends SnugglesEntity> entityType, Level world) {
        super(entityType, world);

        ItemStack equippedStack = TNT.copy();
        this.setItemSlot(EquipmentSlot.CHEST, equippedStack);
        this.setDropChance(EquipmentSlot.CHEST, 0.20F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FollowAndBlowGoal(this, 1.0D, false));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean ret = super.hurt(source, amount);
//        if (!this.isDead() && ret && (amount == 0 || source == DamageSource.ON_FIRE || source == DamageSource.IN_FIRE)) {
//            explode();
//        }
        return ret;
    }

    @Override
    public WeaponizedGolemType getGolemType() {
        return WeaponizedGolemType.SNUGGLES;
    }

    public void explode() {
        if (!this.getLevel().isClientSide()) {
            this.discard();

            ServerLevel world = (ServerLevel) this.getLevel();

            float power = 3.0f;
            Explosion.BlockInteraction destructionType = Explosion.BlockInteraction.NONE;

            Explosion explosion = new PuffExplosion(world, this, DamageSource.explosion(this), null, this.getX(), this.getY(), this.getZ(), power, 3f, destructionType, false);
            explosion.explode();
            explosion.finalizeExplosion(false);

            for (int i = 0; i < 250; i++) {
                FallingBlockEntity entity = FallingBlockEntity.fall(world, this.blockPosition(), Blocks.POWDER_SNOW.defaultBlockState());
                entity.time = 1;
                entity.dropItem = false;
                entity.setPos(this.getX(), this.getY(), this.getZ());
                entity.syncPacketPositionCodec(this.getX(), this.getY() + 0.5f, this.getZ());
                entity.setDeltaMovement(random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
                //fix for forge;
                //world.addFreshEntity(entity);
            }

            Iterator<ServerPlayer> var14 = world.players().iterator();
            if (destructionType == Explosion.BlockInteraction.NONE) {
                explosion.clearToBlow();
            }

            while (var14.hasNext()) {
                ServerPlayer serverPlayerEntity = var14.next();
                if (serverPlayerEntity.distanceToSqr(this.getX(), this.getY(), this.getZ()) < 4096.0D) {
                    serverPlayerEntity.connection.send(new ClientboundExplodePacket(this.getX(), this.getY(), this.getZ(), power, explosion.getToBlow(), explosion.getHitPlayers().get(serverPlayerEntity)));
                }
            }
        }
    }
}
