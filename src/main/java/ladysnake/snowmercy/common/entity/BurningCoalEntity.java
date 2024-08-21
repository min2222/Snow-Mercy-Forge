package ladysnake.snowmercy.common.entity;

import ladysnake.snowmercy.common.init.SnowMercyEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class BurningCoalEntity extends ThrowableProjectile {
    public static final int MELT_RADIUS = 3;
    public static final int BURN_RADIUS = 1;
    private boolean isExtraHot = false;

    public BurningCoalEntity(EntityType<? extends BurningCoalEntity> entityType, Level world) {
        super(entityType, world);
    }

    public BurningCoalEntity(Level world, LivingEntity owner) {
        super(SnowMercyEntities.BURNING_COAL.get(), owner.getX(), owner.getEyeY() - (double) 0.9f, owner.getZ(), world);
        this.setOwner(owner);
    }

    public BurningCoalEntity(Level world, double x, double y, double z) {
        super(SnowMercyEntities.BURNING_COAL.get(), x, y, z, world);
    }

    public boolean isExtraHot() {
        return isExtraHot;
    }

    public void setExtraHot(boolean extraHot) {
        isExtraHot = extraHot;
    }

    @Override
    protected void defineSynchedData() {
    	
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean canCollideWith(Entity other) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        this.setDeltaMovement(this.getDeltaMovement().multiply(0.9, 0.9, 0.9));

        if (this.isInWaterOrRain() || this.isInWall() || (this.tickCount > 20 && this.getDeltaMovement().x <= 0.1f && this.getDeltaMovement().y <= 0.1f && this.getDeltaMovement().z <= 0.1f)) {
            this.discard();
        }

        for (int i = 0; i < Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().y * this.getDeltaMovement().y + this.getDeltaMovement().z * this.getDeltaMovement().z) * 25; i++) {
            level.addParticle(ParticleTypes.FLAME, this.getX() + random.nextGaussian() / 5f, this.getY() + .5 + random.nextGaussian() / 5f, this.getZ() + random.nextGaussian() / 5f, 0, 0, 0);
        }

        // melt snow and ice
        if (!level.isClientSide) {
            for (int x = -MELT_RADIUS; x <= MELT_RADIUS; x++) {
                for (int y = -MELT_RADIUS; y <= MELT_RADIUS; y++) {
                    for (int z = -MELT_RADIUS; z <= MELT_RADIUS; z++) {
                        if (Math.sqrt(this.blockPosition().distToCenterSqr(this.getDeltaMovement().add(x, y, z))) <= MELT_RADIUS) {
                            if (this.isExtraHot()) {
                                if (random.nextInt(5) == 0) {
                                    if (level.getBlockState(this.blockPosition().offset(x, y, z)).getBlock() == Blocks.AIR || level.getBlockState(this.blockPosition().offset(x, y, z)).getBlock() == Blocks.SNOW || level.getBlockState(this.blockPosition().offset(x, y, z)).getBlock() == Blocks.POWDER_SNOW || level.getBlockState(this.blockPosition().offset(x, y, z)).getBlock() == Blocks.SNOW_BLOCK) {
                                    	level.setBlockAndUpdate(this.blockPosition().offset(x, y, z), Blocks.FIRE.defaultBlockState());
                                    }
                                    if (level.getBlockState(this.blockPosition().offset(x, y, z)).getBlock() == Blocks.ICE || level.getBlockState(this.blockPosition().offset(x, y, z)).getBlock() == Blocks.FROSTED_ICE || level.getBlockState(this.blockPosition().offset(x, y, z)).getBlock() == Blocks.PACKED_ICE || level.getBlockState(this.blockPosition().offset(x, y, z)).getBlock() == Blocks.BLUE_ICE) {
                                    	level.setBlockAndUpdate(this.blockPosition().offset(x, y, z), Blocks.WATER.defaultBlockState());
                                        ((ServerLevel) level).sendParticles(ParticleTypes.FALLING_WATER, this.getX() + x, this.getY() + y, this.getZ() + z, 16, random.nextGaussian() / 2f, random.nextGaussian() / 2f, random.nextGaussian() / 2f, 0);
                                    }
                                }
                            } else {
                                if (level.getBlockState(this.blockPosition().offset(x, y, z)).getBlock() == Blocks.SNOW || level.getBlockState(this.blockPosition().offset(x, y, z)).getBlock() == Blocks.POWDER_SNOW) {
                                	level.setBlockAndUpdate(this.blockPosition().offset(x, y, z), Blocks.AIR.defaultBlockState());
                                    ((ServerLevel) level).sendParticles(ParticleTypes.FALLING_WATER, this.getX() + x, this.getY() + y, this.getZ() + z, 16, random.nextGaussian() / 2f, random.nextGaussian() / 2f, random.nextGaussian() / 2f, 0);
                                }
                                if (level.getBlockState(this.blockPosition().offset(x, y, z)).getBlock() == Blocks.ICE || level.getBlockState(this.blockPosition().offset(x, y, z)).getBlock() == Blocks.FROSTED_ICE) {
                                	level.setBlockAndUpdate(this.blockPosition().offset(x, y, z), Blocks.WATER.defaultBlockState());
                                    ((ServerLevel) level).sendParticles(ParticleTypes.FALLING_WATER, this.getX() + x, this.getY() + y, this.getZ() + z, 16, random.nextGaussian() / 2f, random.nextGaussian() / 2f, random.nextGaussian() / 2f, 0);
                                }
                            }
                        }
                    }
                }
            }
        }

        // burn entities in contact
        for (Entity entity : level.getEntities(this, this.getBoundingBox().inflate(BURN_RADIUS), entity -> entity != this.getOwner())) {
            if (!(entity instanceof ItemEntity)) {
                entity.setSecondsOnFire((int) (10 * Math.sqrt(entity.blockPosition().distSqr(this.blockPosition()))));
                entity.hurt(this.damageSources().inFire(), 2f);
            }
        }
    }

    @Override
    public void push(Entity entity) {
    	
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}
