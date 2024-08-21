package ladysnake.snowmercy.common.entity;

import org.jetbrains.annotations.Nullable;

import ladysnake.snowmercy.common.entity.ai.goal.GoToHeartGoal;
import ladysnake.snowmercy.common.entity.ai.goal.WeaponizedSnowGolemFollowTargetGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public abstract class WeaponizedSnowGolemEntity extends PathfinderMob implements SnowMercyEnemy {
    private static final EntityDataAccessor<Integer> HEAD = SynchedEntityData.defineId(WeaponizedSnowGolemEntity.class, EntityDataSerializers.INT);

    private int ageHeadless = 0;

    public WeaponizedSnowGolemEntity(EntityType<? extends WeaponizedSnowGolemEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createEntityAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.ATTACK_DAMAGE).add(Attributes.FOLLOW_RANGE, 48);
    }

    public abstract WeaponizedGolemType getGolemType();

    @Override
    protected void registerGoals() {
    	
        this.targetSelector.addGoal(1, new GoToHeartGoal(this, 1.0f, false, 20));

        // hostile (1) goals = target weaponized snowmen with a pumpkin (2) + snow golems + players
        this.targetSelector.addGoal(1, new WeaponizedSnowGolemFollowTargetGoal(this, WeaponizedSnowGolemEntity.class, 10, true, false, 1, livingEntity -> ((WeaponizedSnowGolemEntity) livingEntity).getHead() != 1));
        this.targetSelector.addGoal(1, new WeaponizedSnowGolemFollowTargetGoal(this, SnowGolem.class, 100, true, false, 1, livingEntity -> !(livingEntity instanceof WeaponizedSnowGolemEntity)));
        this.targetSelector.addGoal(1, new WeaponizedSnowGolemFollowTargetGoal(this, Player.class, true, 1));
        // headless (0) goals = target any living entity
        this.targetSelector.addGoal(1, new WeaponizedSnowGolemFollowTargetGoal(this, LivingEntity.class, 10, true, false, 0, Entity::isAttackable));
        // common goals
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D, 1.0000001E-5F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HEAD, 1);
    }

    // HEAD STATUS
    // 0: Headless
    // 1: Hostile (default)
    // 2: Helps player
    public int getHead() {
        return this.entityData.get(HEAD);
    }

    public void setHead(int head) {
        this.entityData.set(HEAD, head);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getHead() == 0) {
            if (this.ageHeadless++ > SnowGolemHeadEntity.MAX_AGE) {
                this.kill();
            }
        }

        FrostWalkerEnchantment.onEntityMoved(this, this.level, this.blockPosition(), 0);
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    protected void createWitherRose(@Nullable LivingEntity adversary) {
        if (adversary instanceof Player && this.getHead() != 0 && !this.isOnFire() && !this.isNoAi()) {
            this.setHead(0);

            double eyeHeight = this.getY() + this.getEyeHeight(this.getPose(), this.getDimensions(this.getPose())) - 0.3f;
            SnowGolemHeadEntity entity = new SnowGolemHeadEntity(level, this.getGolemType(), this.getX(), eyeHeight, this.getZ());
            entity.setDeltaMovement(this.getDeltaMovement().multiply(1, 0, 1));
            entity.yHeadRot = this.yHeadRot;
            level.addFreshEntity(entity);
        }

        super.createWitherRose(adversary);
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

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!level.isClientSide) {
            if ((source == DamageSource.ON_FIRE || source == DamageSource.IN_FIRE) && this.isAlive()) {
                this.setInvisible(true);
                this.setSilent(true);
                this.setGlowingTag(false);
                ((ServerLevel) level).sendParticles(ParticleTypes.FALLING_WATER, this.getX(), this.getY(), this.getZ(), 10, random.nextGaussian() / 2f, random.nextFloat() * 2f, random.nextGaussian() / 2f, 0);
                this.kill();
            }
        }

        if (!(source.getEntity() instanceof WeaponizedSnowGolemEntity)) {
            if (this.getHead() == 1 && !(this instanceof SnowGolemHeadEntity) && source.getEntity() instanceof LivingEntity) {
                double eyeHeight = this.getY() + this.getEyeHeight(this.getPose(), this.getDimensions(this.getPose())) - 0.3f;
                SnowGolemHeadEntity entity = new SnowGolemHeadEntity(level, this.getGolemType(), this.getX(), eyeHeight, this.getZ());
                LivingEntity livingEntity = ((LivingEntity) source.getEntity());

                if (livingEntity.getMainHandItem().getItem() instanceof ShovelItem && amount >= ((ShovelItem) livingEntity.getMainHandItem().getItem()).getAttackDamage()) {
                    if (!this.level.isClientSide) {
                        this.level.playSound(null, this.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.NEUTRAL, 1.0f, 0.5f);
                        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entity.yHeadRot = this.yHeadRot;
                        entity.setProperties(livingEntity, livingEntity.getXRot(), livingEntity.getYRot(), livingEntity.getFallFlyingTicks(), Math.min(10, amount), Math.min(10, amount));
                        level.addFreshEntity(entity);

                        if (source.getEntity() instanceof Player) {
                            ((Player) source.getEntity()).sweepAttack();
                        }
                        ((ServerLevel) this.level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SNOW_BLOCK, 1)), this.getX(), eyeHeight, this.getZ(), 40, random.nextGaussian() / 20f, 0.2D + random.nextGaussian() / 20f, random.nextGaussian() / 20f, 0.1f);
                    }

                    this.setHead(0);
                    return super.hurt(source, 0.0f);
                }
            }
        } else if (this.getHead() == ((WeaponizedSnowGolemEntity) source.getEntity()).getHead()) {
            return false;
        }

        return super.hurt(source, amount);
    }

    @Override
    public boolean isSensitiveToWater() {
        return false;
    }
    
    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.getHead() == 0 && player.getItemInHand(hand).getItem() == Blocks.CARVED_PUMPKIN.asItem()) {
            player.getItemInHand(hand).shrink(1);
            this.setHead(2);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() && this.getHead() == 2;
    }

    protected boolean shouldDespawnInPeaceful() {
        return this.getHead() != 2;
    }

    @Override
	public boolean shouldDropExperience() {
        return true;
    }

    protected boolean shouldDropLoot() {
        return true;
    }

    public boolean isPreventingPlayerRest(Player player) {
        return this.getHead() == 1;
    }

    public boolean removeWhenFarAway(double distanceSquared) {
        return super.removeWhenFarAway(distanceSquared) && this.getHead() != 2;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SNOW_GOLEM_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SNOW_GOLEM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SNOW_GOLEM_DEATH;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("Head")) {
            this.setHead(tag.getInt("Head"));
        } else {
            this.setHead(1);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("Head", this.getHead());
    }

    @Override
	public int getExperienceReward() {
        return 1 + this.level.random.nextInt(3);
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
