package ladysnake.snowmercy.common.entity;

import ladysnake.snowmercy.common.init.SnowMercyEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class FreezingWindEntity extends ThrowableProjectile {
    public static final int SNOW_RADIUS = 3;
    public static final int FREEZE_RADIUS = 1;

    public FreezingWindEntity(EntityType<? extends FreezingWindEntity> entityType, Level world) {
        super(entityType, world);
    }

    public FreezingWindEntity(Level world, LivingEntity owner) {
        super(SnowMercyEntities.FREEZING_WIND.get(), owner.getX(), owner.getEyeY() - (double) 0.9f, owner.getZ(), world);
        this.setOwner(owner);
    }

    public FreezingWindEntity(Level world, double x, double y, double z) {
        super(SnowMercyEntities.FREEZING_WIND.get(), x, y, z, world);
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
    public void tick() {
        super.tick();

        this.setDeltaMovement(this.getDeltaMovement().multiply(0.9, 0.9, 0.9));

        if (this.isInWaterOrRain() || this.isInWall() || (this.tickCount > 20 && this.getDeltaMovement().x <= 0.1f && this.getDeltaMovement().y <= 0.1f && this.getDeltaMovement().z <= 0.1f)) {
            this.discard();
        }

        for (int i = 0; i < Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().y * this.getDeltaMovement().y + this.getDeltaMovement().z * this.getDeltaMovement().z) * 25; i++) {
            level.addParticle(ParticleTypes.SNOWFLAKE, this.getX() + random.nextGaussian() / 5f, this.getY() + .5 + random.nextGaussian() / 5f, this.getZ() + random.nextGaussian() / 5f, 0, 0, 0);
        }

        if (this.isOnFire()) {
            this.discard();
        }

        // make snow and ice
        for (int x = -SNOW_RADIUS; x <= SNOW_RADIUS; x++) {
            for (int y = -SNOW_RADIUS; y <= SNOW_RADIUS; y++) {
                for (int z = -SNOW_RADIUS; z <= SNOW_RADIUS; z++) {
                    if (Math.sqrt(this.blockPosition().distSqr(this.blockPosition().offset(x, y, z))) <= SNOW_RADIUS) {

                        if (level.getBlockState(this.blockPosition().offset(x, y + 1, z)).getBlock() == Blocks.AIR && level.getBlockState(this.blockPosition().offset(x, y, z)).isRedstoneConductor(level, this.blockPosition().offset(x, y, z))) {
                        	level.setBlockAndUpdate(this.blockPosition().offset(x, y + 1, z), Blocks.SNOW.defaultBlockState());
                        }
                        if (level.getBlockState(this.blockPosition().offset(x, y, z)).getBlock() == Blocks.WATER) {
                        	level.setBlockAndUpdate(this.blockPosition().offset(x, y, z), Blocks.ICE.defaultBlockState());
                        }
                    }
                }
            }
        }

        // freeze entities in contact
        for (Entity entity : level.getEntities(this, this.getBoundingBox().inflate(FREEZE_RADIUS), entity -> entity instanceof LivingEntity && entity != this.getOwner())) {
            entity.setTicksFrozen(entity.getTicksFrozen() + (int) (10 * Math.sqrt(entity.blockPosition().distSqr(this.blockPosition()))));
            entity.setSecondsOnFire(0);
        }
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean displayFireAnimation() 
    {
    	return false;
    }
}
