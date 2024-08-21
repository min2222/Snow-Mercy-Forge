package ladysnake.snowmercy.common.entity;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import ladysnake.snowmercy.common.entity.ai.goal.DeployHeadsGoal;
import ladysnake.snowmercy.common.entity.ai.goal.FollowGoal;
import ladysnake.snowmercy.common.entity.ai.goal.GoToHeartGoal;
import ladysnake.snowmercy.common.entity.ai.goal.HeadmasterMinigunAttackGoal;
import ladysnake.snowmercy.common.entity.ai.goal.ReviveSurgeGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class HeadmasterEntity extends Monster implements IAnimatable, SnowMercyEnemy {
    private static final EntityDataAccessor<Boolean> SHOOTING = SynchedEntityData.defineId(HeadmasterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TURRET_TRANSITION = SynchedEntityData.defineId(HeadmasterEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> TURRET_MODE = SynchedEntityData.defineId(HeadmasterEntity.class, EntityDataSerializers.BOOLEAN);
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public HeadmasterEntity(EntityType<? extends HeadmasterEntity> entityType, Level world) {
        super(entityType, world);
    }
    
    @Override
    public float getStepHeight() 
    {
    	return 2f;
    }

    public static AttributeSupplier.Builder createHeadmasterAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE).add(Attributes.FOLLOW_RANGE, 64.0f).add(Attributes.MAX_HEALTH, 200f).add(Attributes.MOVEMENT_SPEED, 0.2f).add(Attributes.KNOCKBACK_RESISTANCE, 1.0f);
    }

    @Override
    protected void registerGoals() {
        // common goals
        this.targetSelector.addGoal(1, new GoToHeartGoal(this, 1.0f, false, 20));
        this.goalSelector.addGoal(1, new FollowGoal(this, 1.0D, false, 10));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, entity -> true));
        this.goalSelector.addGoal(2, new HeadmasterMinigunAttackGoal(this, 2.0D, 40, 8f, 20, 0));

        this.goalSelector.addGoal(2, new DeployHeadsGoal(this));
        this.goalSelector.addGoal(3, new ReviveSurgeGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D, 1.0000001E-5F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();

        this.entityData.define(SHOOTING, false);
        this.entityData.define(TURRET_MODE, false);
        this.entityData.define(TURRET_TRANSITION, 100);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isTurret()) {
            if (this.getTurretTransitionning() > 0) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.headmaster.transition", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
                this.setTurretTransitionning(this.getTurretTransitionning() - 1);
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.headmaster.turret", ILoopType.EDefaultLoopTypes.LOOP));
            }
        } else {
            if (isShooting()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.headmaster.sendhead", ILoopType.EDefaultLoopTypes.LOOP));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.headmaster.standard", ILoopType.EDefaultLoopTypes.LOOP));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void tick() {
        super.tick();

        // switch to turret mod at a quarter of health
        if (this.getHealth() <= this.getMaxHealth() / 3 && !this.isTurret()) {
            this.setTurret(true);
        }

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos pos = this.blockPosition().offset(x, 0, z);
                if (this.level.getBlockState(pos).getBlock() == Blocks.AIR && Blocks.SNOW.defaultBlockState().canSurvive(this.level, pos)) {
                	level.setBlockAndUpdate(pos, Blocks.SNOW.defaultBlockState());
                }
                if (this.level.getBlockState(pos.offset(0, -1, 0)).getBlock() == Blocks.WATER) {
                	level.setBlockAndUpdate(pos.offset(0, -1, 0), Blocks.FROSTED_ICE.defaultBlockState());
                }
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level.isClientSide) {
            int i;
            int j;
            int k;

            if (!this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                return;
            }

            BlockState blockState = Blocks.SNOW.defaultBlockState();

            for (int l = 0; l < 4; ++l) {
                i = Mth.floor(this.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                j = Mth.floor(this.getY());
                k = Mth.floor(this.getZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
                BlockPos blockPos = new BlockPos(i, j, k);
                if (this.level.getBlockState(blockPos).isAir() && blockState.canSurvive(this.level, blockPos)) {
                    this.level.setBlockAndUpdate(blockPos, blockState);
                }
            }
        }
    }

    public void attack(LivingEntity target, float pullProgress) {
        FreezingWindEntity entity = new FreezingWindEntity(this.level, this);
        entity.setPos(this.getX(), this.getY() + 2f, this.getZ());
        Vec3 vec3d = this.getUpVector(1.0F);
        Quaternion quaternion = new Quaternion(new Vector3f(vec3d), 0f, true);
        Vec3 vec3d2 = this.getViewVector(1.0F);
        Vector3f vector3f = new Vector3f(vec3d2);
        vector3f.transform(quaternion);
        entity.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 3f, 5f);
        level.addFreshEntity(entity);
        this.level.playSound(null, this.blockPosition(), SoundEvents.PLAYER_HURT_FREEZE, SoundSource.HOSTILE, 1.0f, 1.0f);
        this.level.playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.0f, 1.5f);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource) || damageSource == DamageSource.CACTUS || damageSource == DamageSource.DROWN || damageSource == DamageSource.FREEZE || damageSource == DamageSource.FALL || damageSource == DamageSource.CRAMMING;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    public boolean isShooting() {
        return this.entityData.get(SHOOTING);
    }

    public void setShooting(boolean shooting) {
        this.entityData.set(SHOOTING, shooting);
    }

    public boolean isTurret() {
        return this.entityData.get(TURRET_MODE);
    }

    public void setTurret(boolean turret) {
        this.entityData.set(TURRET_MODE, turret);
    }

    public int getTurretTransitionning() {
        return this.entityData.get(TURRET_TRANSITION);
    }

    public void setTurretTransitionning(int transitionning) {
        this.entityData.set(TURRET_TRANSITION, transitionning);
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
    protected void tryAddFrost() {
    }
}
