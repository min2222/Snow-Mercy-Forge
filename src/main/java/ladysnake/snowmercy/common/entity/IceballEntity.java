package ladysnake.snowmercy.common.entity;

import ladysnake.snowmercy.cca.SnowMercyComponents;
import ladysnake.snowmercy.common.entity.ai.goal.GoToHeartGoal;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class IceballEntity extends Slime implements SnowMercyEnemy {
    public static final ItemParticleOption PARTICLE = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.ICE));

    public IceballEntity(EntityType<? extends Slime> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createIceballAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 64.0f);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new GoToHeartGoal(this, 1.0f, false, 20));
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        if (fallDistance >= 5f) {
            for (int i = 0; i < this.getSize() * this.getSize() * 10; i++) {
                IcicleEntity entity = new IcicleEntity(level, this);
                entity.setPos(this.getX(), this.getEyeY(), this.getZ());
                entity.syncPacketPositionCodec(this.getX(), this.getY() + 0.5f, this.getZ());
                entity.setDeltaMovement(random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
                level.addFreshEntity(entity);
            }

            this.discard();
        }
        return false;
    }

    @Override
    public boolean isPersistenceRequired()
    {
    	return true;
    }

    @Override
    public void tick() {
        super.tick();
        
        //TODO
        int eventWave = SnowMercyComponents.SNOWMERCY.getEventWave() + 1;
        if (this.getSize() < eventWave) {
            this.setSize(eventWave, true);
        }

        if (this.onGround && this.getTarget() != null && Math.sqrt(this.getTarget().blockPosition().distSqr(this.blockPosition())) <= this.getSize() * 3f) {
            for (int j = 0; j < eventWave * 8; ++j) {
                float f = this.random.nextFloat() * ((float) Math.PI * 2);
                float g = this.random.nextFloat() * 0.5f + 0.5f;
                float h = Mth.sin(f) * (float) eventWave * 0.5f * g;
                float k = Mth.cos(f) * (float) eventWave * 0.5f * g;
                this.level.addParticle(this.getParticleType(), this.getX() + (double) h, this.getY(), this.getZ() + (double) k, 0.0, 0.0, 0.0);
            }
            this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f) / 0.8f);
            this.targetSquish = -0.5f;

            this.setDeltaMovement(getDeltaMovement().x, Math.max(1.0f, this.getSize() * 0.5f), getDeltaMovement().z);
        }
    }

    @Override
    protected ParticleOptions getParticleType() {
        return PARTICLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GLASS_BREAK;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PLAYER_HURT_FREEZE;
    }

    @Override
    protected SoundEvent getJumpSound() {
        return SoundEvents.SNOW_BREAK;
    }

    @Override
    protected SoundEvent getSquishSound() {
        return SoundEvents.GLASS_BREAK;
    }


    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean isFreezing() {
        return false;
    }

    @Override
    protected void tryAddFrost() 
    {
    	
    }

    @Override
    public void remove(RemovalReason reason) {
        this.setRemoved(reason);
        if (reason == RemovalReason.KILLED) {
            this.gameEvent(GameEvent.ENTITY_DIE);
        }
    }

}
