package ladysnake.snowmercy.common.entity;

import ladysnake.snowmercy.common.init.SnowMercyDamageSources;
import ladysnake.snowmercy.common.init.SnowMercyEntities;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class IcicleEntity extends AbstractArrow {
    public IcicleEntity(EntityType<? extends IcicleEntity> entityType, Level world) {
        super(entityType, world);
        this.setSoundEvent(SoundEvents.GLASS_BREAK);
    }

    public IcicleEntity(Level world, LivingEntity owner) {
        super(SnowMercyEntities.ICICLE.get(), world);
        this.setOwner(owner);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        if (hitResult.getType() != HitResult.Type.MISS && !this.level.isClientSide) {
            ((ServerLevel) this.level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.PACKED_ICE, 1)), this.getX(), this.getY(), this.getZ(), 8, random.nextGaussian() / 20f, random.nextGaussian() / 20f, random.nextGaussian() / 20f, random.nextGaussian() / 10f);
        }
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.GLASS_BREAK;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount <= 1) {
            this.level.addParticle(ParticleTypes.POOF, this.getX() + random.nextGaussian() / 20f, this.getY() + random.nextGaussian() / 20f, this.getZ() + random.nextGaussian() / 20f, random.nextGaussian() / 20f, 0.2D + random.nextGaussian() / 20f, random.nextGaussian() / 20f);
        }

        if (this.inGround) {
            this.discard();
        }

        if (this.isOnFire() && !level.isClientSide) {
        	((ServerLevel) this.level).sendParticles(ParticleTypes.FALLING_WATER, this.getX(), this.getY(), this.getZ(), 8, random.nextGaussian() / 5f, random.nextGaussian() / 5f, random.nextGaussian() / 5f, 0);
            this.discard();
        }
    }


    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();

        if (!(entity.isInvulnerable() || entity instanceof SnowMercyEnemy || entity instanceof IceHeartEntity)) {
            Entity entity2 = this.getOwner();
            DamageSource damageSource2;
            if (entity2 == null) {
                damageSource2 = SnowMercyDamageSources.icicle(this, this);
            } else {
                damageSource2 = SnowMercyDamageSources.icicle(this, entity2);
                if (entity2 instanceof LivingEntity) {
                    ((LivingEntity) entity2).setLastHurtMob(entity);
                }
            }

            boolean bl = entity.getType() == EntityType.ENDERMAN;
            int j = entity.getRemainingFireTicks();
            if (this.isOnFire() && !bl) {
                entity.setSecondsOnFire(5);
            }

            if (entity.hurt(damageSource2, (float) this.getBaseDamage())) {
                if (bl) {
                    return;
                }

                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;

                    if (!this.level.isClientSide && entity2 instanceof LivingEntity) {
                        EnchantmentHelper.doPostHurtEffects(livingEntity, entity2);
                        EnchantmentHelper.doPostDamageEffects((LivingEntity) entity2, livingEntity);
                    }

                    this.doPostHurtEffects(livingEntity);
                    if (entity2 != null && livingEntity != entity2 && livingEntity instanceof Player && entity2 instanceof ServerPlayer && !this.isSilent()) {
                        ((ServerPlayer) entity2).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, ClientboundGameEventPacket.DEMO_PARAM_INTRO));
                    }

                    livingEntity.setTicksFrozen(livingEntity.getTicksFrozen() + 100);
                }
            } else {
                entity.setSecondsOnFire(j);
                this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
                this.setYRot(this.getYRot() + 180.0F);
                this.yRotO += 180.0F;
                if (!this.level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                    if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
                        this.spawnAtLocation(this.getPickupItem(), 0.1F);
                    }

                    this.discard();
                }
            }

            this.level.playSound(null, this.blockPosition(), SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH, SoundSource.NEUTRAL, 1.0f, 1.5f);
        } else {
            this.discard();
        }
    }
}
