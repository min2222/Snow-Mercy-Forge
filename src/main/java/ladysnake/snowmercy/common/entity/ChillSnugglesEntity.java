package ladysnake.snowmercy.common.entity;

import java.util.Iterator;

import ladysnake.snowmercy.common.world.PuffExplosion;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class ChillSnugglesEntity extends SnugglesEntity {
    public ChillSnugglesEntity(EntityType<ChillSnugglesEntity> entityType, Level world) {
        super(entityType, world);
    }

    public void explode() {
        if (!this.level.isClientSide()) {
            this.discard();

            ServerLevel world = (ServerLevel) this.level;

            float power = 3.0f;
            Explosion.BlockInteraction destructionType = Explosion.BlockInteraction.KEEP;

            Explosion explosion = new PuffExplosion(world, this, this.damageSources().explosion(this, this), null, this.getX(), this.getY(), this.getZ(), power, 3f, destructionType, false);
            explosion.explode();
            explosion.finalizeExplosion(false);

            for (int i = 0; i < 250; i++) {
                IcicleEntity entity = new IcicleEntity(world, this);
                entity.setPos(this.getX(), this.getY(), this.getZ());
                entity.syncPacketPositionCodec(this.getX(), this.getY() + 0.5f, this.getZ());
                entity.setDeltaMovement(random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
                world.addFreshEntity(entity);
            }

            Iterator<ServerPlayer> var14 = world.players().iterator();
            if (destructionType == Explosion.BlockInteraction.KEEP) {
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

    @Override
    public WeaponizedGolemType getGolemType() {
        return WeaponizedGolemType.CHILL_SNUGGLES;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SNOW_GOLEM_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.GLASS_BREAK;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.GLASS_BREAK;
    }
}
