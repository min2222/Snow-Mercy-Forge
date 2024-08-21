package ladysnake.snowmercy.common.entity;

import ladysnake.snowmercy.common.init.SnowMercyEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class SnowGolemHeadEntity extends WeaponizedSnowGolemEntity {
    public static final int MAX_AGE = 600;

    private static final EntityDataAccessor<WeaponizedGolemType> GOLEM_TYPE = SynchedEntityData.defineId(SnowGolemHeadEntity.class, WeaponizedGolemType.TRACKED_DATA_HANDLER);

    public SnowGolemHeadEntity(EntityType<? extends SnowGolemHeadEntity> entityType, Level world) {
        super(entityType, world);
    }

    public SnowGolemHeadEntity(Level world, WeaponizedGolemType golemType, double x, double y, double z) {
        super(SnowMercyEntities.SNOW_GOLEM_HEAD.get(), world);
        this.absMoveTo(x, y, z);
        this.setGolemType(golemType);
    }

    public static AttributeSupplier.Builder createEntityAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 1.0D).add(Attributes.MOVEMENT_SPEED, 0.0D).add(Attributes.ATTACK_DAMAGE, 0.0D);
    }

    protected void initGoals() {
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GOLEM_TYPE, WeaponizedGolemType.DEFAULT);
    }

    public WeaponizedGolemType getGolemType() {
        return this.entityData.get(GOLEM_TYPE);
    }

    public void setGolemType(WeaponizedGolemType golemType) {
        this.entityData.set(GOLEM_TYPE, golemType);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putString("golemType", this.getGolemType().getId().toString());
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("golemType", Tag.TAG_STRING)) {
            this.setGolemType(WeaponizedGolemType.byId(ResourceLocation.tryParse(tag.getString("golemType"))));
        } else if (tag.contains("golemType", Tag.TAG_INT)) {
            // migrate from old versions
            this.setGolemType(WeaponizedGolemType.values()[tag.getInt("golemType") % WeaponizedGolemType.values().length]);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level.isClientSide()) {
            ((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SNOW_BLOCK, 1)), this.getX(), this.getY() + 0.4f, this.getZ(), 40, 0f, 0f, 0f, 0.1f);
        }
        this.level.playSound(null, this.blockPosition(), SoundEvents.SNOW_GOLEM_DEATH, SoundSource.NEUTRAL, 1.0f, 1.0f);
        this.discard();

        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level.getBlockState(this.blockPosition().offset(0d, -1d, 0d)).getBlock() == Blocks.SNOW_BLOCK
                && this.level.getBlockState(this.blockPosition().offset(0d, -2d, 0d)).getBlock() == Blocks.SNOW_BLOCK) {
            this.hurt(DamageSource.GENERIC, 1.0f);
            this.level.destroyBlock(this.blockPosition().offset(0d, -1d, 0d), false);
            this.level.destroyBlock(this.blockPosition().offset(0d, -2d, 0d), false);
            WeaponizedSnowGolemEntity golem = this.getGolemType().getEntityType().create(this.level);
            BlockPos blockPos = this.blockPosition().offset(0d, -2d, 0d);
            assert golem != null;
            golem.moveTo((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.05D, (double) blockPos.getZ() + 0.5D, 0.0F, 0.0F);
            level.addFreshEntity(golem);
        }

        if (this.tickCount >= MAX_AGE) {
            this.hurt(DamageSource.GENERIC, 1.0f);
        }
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);

        double speed = Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().y * this.getDeltaMovement().y + this.getDeltaMovement().z * this.getDeltaMovement().z);

        if (speed > 0.5f) {
            player.hurt(DamageSource.indirectMobAttack(this, this), (float) speed);
        }
    }

    public void setProperties(Entity user, float pitch, float yaw, float roll, float modifierZ, float modifierXYZ) {
        float f = -Mth.sin(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
        float g = -Mth.sin((pitch + roll) * 0.017453292F);
        float h = Mth.cos(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
        this.setVelocity(f, g, h, modifierZ, modifierXYZ);
        Vec3 vec3d = user.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3d.x, user.isOnGround() ? 0.0D : vec3d.y, vec3d.z));
    }

    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        Vec3 vec3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * 0.007499999832361937D * (double) divergence, this.random.nextGaussian() * 0.007499999832361937D * (double) divergence, this.random.nextGaussian() * 0.007499999832361937D * (double) divergence).scale(speed);
        this.setDeltaMovement(vec3d);
        float f = Mth.sqrt((float) (vec3d.x * vec3d.x + vec3d.z * vec3d.z));
        this.setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * 57.2957763671875D));
        this.setXRot((float) (Mth.atan2(vec3d.y, f) * 57.2957763671875D));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public boolean isSensitiveToWater()
    {
    	return true;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.5f;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }
}
