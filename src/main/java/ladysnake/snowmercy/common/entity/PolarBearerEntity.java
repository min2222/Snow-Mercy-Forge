package ladysnake.snowmercy.common.entity;

import org.jetbrains.annotations.Nullable;

import ladysnake.snowmercy.common.entity.ai.goal.GoToHeartGoal;
import ladysnake.snowmercy.common.init.SnowMercyEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;

public class PolarBearerEntity extends PolarBear implements SnowMercyEnemy {
    public PolarBearerEntity(EntityType<? extends PolarBear> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createPolarBearerAttributes() {
        return PolarBear.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.25f).add(Attributes.FOLLOW_RANGE, 64.0f);
    }
    
    @Override
    public float getStepHeight() 
    {
    	return 1f;
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new GoToHeartGoal(this, 1.0f, false, 20));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AttackGoal());
        this.goalSelector.addGoal(1, new PolarBearEscapeDangerGoal());
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new PolarBearRevengeGoal());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<Player>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<PolarBear>(this, false));
    }

    @Override
    public boolean isAngryAtAllPlayers(Level world) {
        return true;
    }

    @Override
    public boolean isAngryAt(LivingEntity entity) {
        if (!this.canAttack(entity)) {
            return false;
        }
        if (entity.getType() == EntityType.PLAYER && this.isAngryAtAllPlayers(entity.level)) {
            return true;
        }
        return entity.getUUID().equals(this.getPersistentAngerTarget());
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (target instanceof WeaponizedSnowGolemEntity && ((WeaponizedSnowGolemEntity) target).getHead() == 1 || target instanceof PolarBear) {
            return false;
        }

        return super.canAttack(target);
    }

    //TODO
    /*@Override
    public boolean canBeControlledByRider() {
        return !this.isNoAi() && this.getControllingPassenger() instanceof LivingEntity;
    }*/

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        return (@Nullable LivingEntity) this.getFirstPassenger();
    }

    @Override
    public void tick() {
        super.tick();

        if (!level.isClientSide) {
            if (this.age <= 5 && this.getPassengers().isEmpty()) {
                RocketsEntity rider = SnowMercyEntities.ROCKETS.get().create(level);
                ForgeEventFactory.onFinalizeSpawn(rider, (ServerLevelAccessor) level, level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.JOCKEY, null, null);
                rider.setPos(this.getX(), this.getY(), this.getZ());
                rider.setPersistenceRequired();
                rider.startRiding(this);
                level.addFreshEntity(rider);
            }
        }
    }

    @Override
    public boolean isPersistenceRequired() 
    {
    	return true;
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

    class AttackGoal
            extends MeleeAttackGoal {
        public AttackGoal() {
            super(PolarBearerEntity.this, 1.25, true);
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity target, double squaredDistance) {
            double d = this.getAttackReachSqr(target);
            if (squaredDistance <= d && this.isTimeToAttack()) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(target);
                PolarBearerEntity.this.setStanding(false);
            } else if (squaredDistance <= d * 2.0) {
                if (this.isTimeToAttack()) {
                    PolarBearerEntity.this.setStanding(false);
                    this.resetAttackCooldown();
                }
                if (this.getTicksUntilNextAttack() <= 10) {
                    PolarBearerEntity.this.setStanding(true);
                    PolarBearerEntity.this.playWarningSound();
                }
            } else {
                this.resetAttackCooldown();
                PolarBearerEntity.this.setStanding(false);
            }
        }

        @Override
        public void stop() {
            PolarBearerEntity.this.setStanding(false);
            super.stop();
        }

        @Override
        protected double getAttackReachSqr(LivingEntity entity) {
            return 4.0f + entity.getBbWidth();
        }
    }

    class PolarBearEscapeDangerGoal
            extends PanicGoal {
        public PolarBearEscapeDangerGoal() {
            super(PolarBearerEntity.this, 2.0);
        }

        @Override
        public boolean canUse() {
            if (!PolarBearerEntity.this.isBaby() && !PolarBearerEntity.this.isOnFire()) {
                return false;
            }
            return super.canUse();
        }
    }

    class PolarBearRevengeGoal
            extends HurtByTargetGoal {
        public PolarBearRevengeGoal() {
            super(PolarBearerEntity.this, new Class[0]);
        }
	
        @Override
        public void start() {
            super.start();
            if (PolarBearerEntity.this.isBaby()) {
                this.alertOthers();
                this.stop();
            }
        }

        @Override
        protected void alertOther(Mob mob, LivingEntity target) {
            if (mob instanceof PolarBear && !mob.isBaby()) {
                super.alertOther(mob, target);
            }
        }
    }

}
