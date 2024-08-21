package ladysnake.snowmercy.common.entity;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import ladysnake.snowmercy.common.init.SnowMercyDamageSources;
import ladysnake.snowmercy.common.init.SnowMercyEntities;
import ladysnake.snowmercy.common.init.SnowMercyItems;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;

public class SledgeEntity extends Entity {
    public static final int field_30697 = 0;
    public static final int field_30698 = 1;
    public static final double field_30699 = 0.7853981852531433;
    public static final int field_30700 = 60;
    private static final EntityDataAccessor<Integer> DAMAGE_WOBBLE_TICKS = SynchedEntityData.defineId(SledgeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DAMAGE_WOBBLE_SIDE = SynchedEntityData.defineId(SledgeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DAMAGE_WOBBLE_STRENGTH = SynchedEntityData.defineId(SledgeEntity.class, EntityDataSerializers.FLOAT);
    private static final int field_30695 = 60;
    private static final double field_30696 = (double) 0.3926991f;
    private float velocityDecay;
    private float ticksUnderwater;
    private float yawVelocity;
    private int field_7708;
    private double x;
    private double y;
    private double z;
    private double sledgeYaw;
    private double sledgePitch;
    private boolean pressingLeft;
    private boolean pressingRight;
    private boolean pressingForward;
    private boolean pressingBack;
    private float field_7714;
    private Location location;
    private Location lastLocation;
    private double fallVelocity;
    private double prevX;
    private double prevY;
    private double prevZ;

    public SledgeEntity(EntityType<? extends SledgeEntity> entityType, Level world) {
        super(entityType, world);
        this.blocksBuilding = true;
    }
    
    @Override
    public float getStepHeight() 
    {
    	return 1.0f;
    }

    public SledgeEntity(Level world, double x, double y, double z) {
        this(SnowMercyEntities.SLEDGE.get(), world);
        this.setPos(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public static boolean canCollide(Entity entity, Entity other) {
        return (other.canBeCollidedWith() || other.isPushable()) && !entity.isPassengerOfSameVehicle(other);
    }

    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DAMAGE_WOBBLE_TICKS, 0);
        this.entityData.define(DAMAGE_WOBBLE_SIDE, 1);
        this.entityData.define(DAMAGE_WOBBLE_STRENGTH, Float.valueOf(0.0f));
    }

    @Override
    public boolean canCollideWith(Entity other) {
        return SledgeEntity.canCollide(this, other);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected Vec3 getRelativePortalPosition(Direction.Axis portalAxis ,BlockUtil.FoundRectangle portalRect) {
        return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(portalAxis, portalRect));
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean bl;
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (this.level.isClientSide || this.isRemoved()) {
            return true;
        }
        this.setDamageWobbleSide(-this.getDamageWobbleSide());
        this.setDamageWobbleTicks(10);
        this.setDamageWobbleStrength(this.getDamageWobbleStrength() + amount * 10.0f);
        this.markHurt();
        this.gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
        boolean bl2 = bl = source.getEntity() instanceof Player && ((Player) source.getEntity()).getAbilities().instabuild;
        if (bl || this.getDamageWobbleStrength() > 40.0f) {
            if (!bl && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                this.spawnAtLocation(this.asItem());
            }
            this.discard();
        }
        return true;
    }

    @Override
    public void onAboveBubbleCol(boolean drag) {
        this.level.addParticle(ParticleTypes.SPLASH, this.getX() + (double) this.random.nextFloat(), this.getY() + 0.7, this.getZ() + (double) this.random.nextFloat(), 0.0, 0.0, 0.0);
        if (this.random.nextInt(20) == 0) {
            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), this.getSwimSplashSound(), this.getSoundSource(), 1.0f, 0.8f + 0.4f * this.random.nextFloat(), false);
        }
        this.gameEvent(GameEvent.SPLASH, this.getControllingPassenger());
    }

    @Override
    public void push(Entity entity) {
        if (entity instanceof SledgeEntity) {
            if (entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
                super.push(entity);
            }
        } else if (entity.getBoundingBox().minY <= this.getBoundingBox().minY) {
            super.push(entity);
        }
    }

    public Item asItem() {
        return SnowMercyItems.SLEDGE.get();
    }

    @Override
    public void animateHurt() {
        this.setDamageWobbleSide(-this.getDamageWobbleSide());
        this.setDamageWobbleTicks(10);
        this.setDamageWobbleStrength(this.getDamageWobbleStrength() * 11.0f);
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.sledgeYaw = yaw;
        this.sledgePitch = pitch;
        this.field_7708 = 10;
    }

    @Override
    public Direction getMotionDirection() {
        return this.getDirection().getClockWise();
    }

    @Override
    public void tick() {
        this.lastLocation = this.location;
        this.location = this.checkLocation();
        this.ticksUnderwater = this.location == Location.UNDER_WATER || this.location == Location.UNDER_FLOWING_WATER ? (this.ticksUnderwater += 1.0f) : 0.0f;
        if (!this.level.isClientSide && this.ticksUnderwater >= 60.0f) {
            this.ejectPassengers();
        }
        if (this.getDamageWobbleTicks() > 0) {
            this.setDamageWobbleTicks(this.getDamageWobbleTicks() - 1);
        }
        if (this.getDamageWobbleStrength() > 0.0f) {
            this.setDamageWobbleStrength(this.getDamageWobbleStrength() - 1.0f);
        }
        super.tick();
        this.method_7555();
        if (this.isControlledByLocalInstance()) {
            this.updateVelocity();
            if (this.level.isClientSide) {
                this.updatePaddles();
            }
            this.move(MoverType.SELF, this.getDeltaMovement());


        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }

        this.checkInsideBlocks();
        List<Entity> i = this.level.getEntities(this, this.getBoundingBox().inflate(0.2f, -0.01f, 0.2f), EntitySelector.pushableBy(this));
        if (!i.isEmpty()) {
            boolean soundEvent = !this.level.isClientSide && !(this.getControllingPassenger() instanceof Player);
            for (int vec3d = 0; vec3d < i.size(); ++vec3d) {
                Entity d = i.get(vec3d);
                if (d.hasPassenger(this)) continue;
                if (soundEvent && this.getPassengers().size() < 2 && !d.isPassenger() && d.getBbWidth() < this.getBbWidth() && d instanceof LivingEntity && !(d instanceof WaterAnimal) && !(d instanceof Player)) {
                    d.startRiding(this);
                    continue;
                }
                this.push(d);
            }
        }

        // remove powder snow around
        if (this.getDeltaMovement().x != 0 && this.getDeltaMovement().z != 0) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos nextPos = this.blockPosition().offset(x, 0, z);
                    if (level.getBlockState(nextPos).getBlock() == Blocks.POWDER_SNOW) {
                        level.destroyBlock(nextPos, false);
                    }
                }
            }
        }

        // ram snowmen
        if (!this.level.isClientSide && this.getControllingPassenger() instanceof Player) {
            double velX = prevX - this.getControllingPassenger().trackingPosition().x;
            double velZ = prevZ - this.getControllingPassenger().trackingPosition().z;

            double currentVelocity = Math.sqrt(velX * velX + velZ * velZ);

            prevX = this.getControllingPassenger().trackingPosition().x;
            prevZ = this.getControllingPassenger().trackingPosition().z;

            if (currentVelocity > 0.5f) {
                for (LivingEntity roadkill : level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1f), livingEntity -> livingEntity.isAlive() && !this.getPassengers().contains(livingEntity))) {
                    if (roadkill instanceof WeaponizedSnowGolemEntity) {
                        ((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Blocks.SNOW_BLOCK)), roadkill.getX(), roadkill.getY(), roadkill.getZ(), 200, random.nextGaussian() / 3f, random.nextGaussian() + 1 / 3f, random.nextGaussian() / 3f, 0.2f);
                        roadkill.kill();
                    } else {
                        roadkill.hurt(SnowMercyDamageSources.ramming(this.getControllingPassenger()), (float) (currentVelocity * 10f));
                    }
                }
            }
        }


    }

    public int method_33588() {
        return this.getId() * 3;
    }

    private void method_7555() {
        if (this.isControlledByLocalInstance()) {
            this.field_7708 = 0;
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }
        if (this.field_7708 <= 0) {
            return;
        }
        double d = this.getX() + (this.x - this.getX()) / (double) this.field_7708;
        double e = this.getY() + (this.y - this.getY()) / (double) this.field_7708;
        double f = this.getZ() + (this.z - this.getZ()) / (double) this.field_7708;
        double g = MathHelper.wrapDegrees(this.sledgeYaw - (double) this.getYRot());
        this.setYRot(this.getYRot() + (float) g / (float) this.field_7708);
        this.setXRot(this.getXRot() + (float) (this.sledgePitch - (double) this.getXRot()) / (float) this.field_7708);
        --this.field_7708;
        this.setPos(d, e, f);
        this.setRot(this.getYRot(), this.getXRot());
    }

    private Location checkLocation() {
        Location location = this.getUnderWaterLocation();
        if (location != null) {
            return location;
        }
        float f = this.method_7548();
        if (f > 0.0f) {
            this.field_7714 = f;
            return Location.ON_LAND;
        }
        return Location.IN_AIR;
    }

    public float method_7544() {
        AABB box = this.getBoundingBox();
        int i = Mth.floor(box.minX);
        int j = Mth.ceil(box.maxX);
        int k = Mth.floor(box.maxY);
        int l = Mth.ceil(box.maxY - this.fallVelocity);
        int m = Mth.floor(box.minZ);
        int n = Mth.ceil(box.maxZ);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        block0:
        for (int o = k; o < l; ++o) {
            float f = 0.0f;
            for (int p = i; p < j; ++p) {
                for (int q = m; q < n; ++q) {
                    mutable.set(p, o, q);
                    FluidState fluidState = this.level.getFluidState(mutable);
                    if (fluidState.is(FluidTags.WATER)) {
                        f = Math.max(f, fluidState.getHeight(this.level, mutable));
                    }
                    if (f >= 1.0f) continue block0;
                }
            }
            if (!(f < 1.0f)) continue;
            return (float) mutable.getY() + f;
        }
        return l + 1;
    }

    public float method_7548() {
    	AABB box = this.getBoundingBox();
    	AABB box2 = new AABB(box.minX, box.minY - 0.001, box.minZ, box.maxX, box.minY, box.maxZ);
        int i = Mth.floor(box2.minX) - 1;
        int j = Mth.ceil(box2.maxX) + 1;
        int k = Mth.floor(box2.minY) - 1;
        int l = Mth.ceil(box2.maxY) + 1;
        int m = Mth.floor(box2.minZ) - 1;
        int n = Mth.ceil(box2.maxZ) + 1;
        VoxelShape voxelShape = Shapes.create(box2);
        float f = 0.0f;
        int o = 0;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int p = i; p < j; ++p) {
            for (int q = m; q < n; ++q) {
                int r = (p == i || p == j - 1 ? 1 : 0) + (q == m || q == n - 1 ? 1 : 0);
                if (r == 2) continue;
                for (int s = k; s < l; ++s) {
                    if (r > 0 && (s == k || s == l - 1)) continue;
                    mutable.set(p, s, q);
                    BlockState blockState = this.level.getBlockState(mutable);
                    if (blockState.getBlock() instanceof WaterlilyBlock || !Shapes.joinIsNotEmpty(blockState.getCollisionShape(this.level, mutable).move(p, s, q), voxelShape, BooleanOp.AND))
                        continue;
                    f += blockState.getBlock().getFriction();
                    ++o;
                }
            }
        }
        return f / (float) o;
    }

    @Nullable
    private Location getUnderWaterLocation() {
        AABB box = this.getBoundingBox();
        double d = box.maxY + 0.001;
        int i = Mth.floor(box.minX);
        int j = Mth.ceil(box.maxX);
        int k = Mth.floor(box.maxY);
        int l = Mth.ceil(d);
        int m = Mth.floor(box.minZ);
        int n = Mth.ceil(box.maxZ);
        boolean bl = false;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int o = i; o < j; ++o) {
            for (int p = k; p < l; ++p) {
                for (int q = m; q < n; ++q) {
                    mutable.set(o, p, q);
                    FluidState fluidState = this.level.getFluidState(mutable);
                    if (!fluidState.is(FluidTags.WATER) || !(d < (double) ((float) mutable.getY() + fluidState.getHeight(this.level, mutable))))
                        continue;
                    if (fluidState.isSource()) {
                        bl = true;
                        continue;
                    }
                    return Location.UNDER_FLOWING_WATER;
                }
            }
        }
        return bl ? Location.UNDER_WATER : null;
    }

    private void updateVelocity() {
        double d = -0.04f;
        double e = this.isNoGravity() ? 0.0 : (double) -0.04f;
        double f = 0.0;
        this.velocityDecay = 0.05f;

        boolean isOnSnow = false;
        boolean isOnIce = false;

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -1; y <= 0; y++) {
                    BlockPos checkedPos = blockPosition().offset(x, y, z);
                    if (this.level.getBlockState(checkedPos).getMaterial() == Material.SNOW || this.level.getBlockState(checkedPos).getMaterial() == Material.TOP_SNOW) {
                        isOnSnow = true;
                    }
                }
            }
        }

        if (!isOnSnow) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    for (int y = -1; y <= 0; y++) {
                        BlockPos checkedPos = blockPosition().offset(x, y, z);
                        if (this.level.getBlockState(checkedPos).getMaterial() == Material.ICE || this.level.getBlockState(checkedPos).getMaterial() == Material.ICE_SOLID) {
                            isOnIce = true;
                        }
                    }
                }
            }
        }

        if (this.level.isClientSide && Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z) > 0.05f) {
            if (this.tickCount % 20 == 0) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, this.getSoundSource(), 0.5f, 0.95f + this.random.nextFloat() * 0.05f, false);
            }
            if (this.tickCount % 5 == 0) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.POWDER_SNOW_STEP, this.getSoundSource(), 0.95f + this.random.nextFloat() * 0.05f, 0.8f, false);
            }


            float h = Mth.cos(this.getYRot() * ((float) Math.PI / 180)) * 0.35f;
            float j = Mth.sin(this.getYRot() * ((float) Math.PI / 180)) * 0.35f;


            this.level.addParticle(ParticleTypes.FIREWORK, this.getX() + (double) h, this.getY() + .2f, this.getZ() + (double) j, 0.0, 0.0, 0.0);
            this.level.addParticle(ParticleTypes.FIREWORK, this.getX() - (double) h, this.getY() + .2f, this.getZ() - (double) j, 0.0, 0.0, 0.0);
        }

        if (this.location == Location.IN_AIR) {
            this.velocityDecay = 0.95f;
        } else if (this.location == Location.ON_LAND) {
            if (isOnIce) {
                this.velocityDecay = 0.975f;
            } else if (isOnSnow) {
                this.velocityDecay = 0.95f;
            }
            if (this.getControllingPassenger() instanceof Player) {
                this.field_7714 /= 2.0f;
            }
        }
        Vec3 vec3d = this.getDeltaMovement();
        this.setDeltaMovement(vec3d.x * (double) this.velocityDecay, vec3d.y + e, vec3d.z * (double) this.velocityDecay);
        this.yawVelocity *= this.velocityDecay;
        if (f > 0.0) {
            Vec3 vec3d2 = this.getDeltaMovement();
            this.setDeltaMovement(vec3d2.x, (vec3d2.y + f * 0.06153846016296973) * 0.75, vec3d2.z);
        }
    }

    private void updatePaddles() {
        if (!this.isVehicle()) {
            return;
        }
        float f = 0.0f;
        if (this.pressingLeft) {
            this.yawVelocity -= 1.0f;
        }
        if (this.pressingRight) {
            this.yawVelocity += 1.0f;
        }
        if (this.pressingRight != this.pressingLeft && !this.pressingForward && !this.pressingBack) {
            f += 0.005f;
        }
        this.setYRot(this.getYRot() + this.yawVelocity);
        if (this.pressingForward) {
            f += 0.04f;
        }
        if (this.pressingBack) {
            f -= 0.005f;
        }
        this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(-this.getYRot() * ((float) Math.PI / 180)) * f, 0.0, Mth.cos(this.getYRot() * ((float) Math.PI / 180)) * f));
    }

    @Override
    public void positionRider(Entity passenger) {
        if (!this.hasPassenger(passenger)) {
            return;
        }
        float f = 0.0f;
        float g = (float) ((this.isRemoved() ? (double) 0.01f : this.getPassengersRidingOffset()) + passenger.getMyRidingOffset());
        if (this.getPassengers().size() > 1) {
            int i = this.getPassengers().indexOf(passenger);
            f = i == 0 ? 0.2f : -0.6f;
            if (passenger instanceof Animal) {
                f = (float) ((double) f + 0.2);
            }
        }
        Vec3 i = new Vec3(f, 0.0, 0.0).yRot(-this.getYRot() * ((float) Math.PI / 180) - 1.5707964f);
        passenger.setPos(this.getX() + i.x, this.getY() + (double) g, this.getZ() + i.z);
        passenger.setYRot(passenger.getYRot() + this.yawVelocity);
        passenger.setYHeadRot(passenger.getYHeadRot() + this.yawVelocity);
        this.copyEntityData(passenger);
        if (passenger instanceof Animal && this.getPassengers().size() > 1) {
            int j = passenger.getId() % 2 == 0 ? 90 : 270;
            passenger.setYBodyRot(((Animal) passenger).yBodyRot + (float) j);
            passenger.setYHeadRot(passenger.getYHeadRot() + (float) j);
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        double e;
        Vec3 vec3d = SledgeEntity.getCollisionHorizontalEscapeVector(this.getBbWidth() * Mth.SQRT_OF_TWO, passenger.getBbWidth(), passenger.getYRot());
        double d = this.getX() + vec3d.x;
        BlockPos blockPos = new BlockPos(d, this.getBoundingBox().maxY, e = this.getZ() + vec3d.z);
        BlockPos blockPos2 = blockPos.below();
        if (!this.level.isWaterAt(blockPos2)) {
            double g;
            ArrayList<Vec3> list = Lists.newArrayList();
            double f = this.level.getBlockFloorHeight(blockPos);
            if (DismountHelper.isBlockFloorValid(f)) {
                list.add(new Vec3(d, (double) blockPos.getY() + f, e));
            }
            if (DismountHelper.isBlockFloorValid(g = this.level.getBlockFloorHeight(blockPos2))) {
                list.add(new Vec3(d, (double) blockPos2.getY() + g, e));
            }
            for (Pose entityPose : passenger.getDismountPoses()) {
                for (Vec3 vec3d2 : list) {
                    if (!DismountHelper.canDismountTo(this.level, vec3d2, passenger, entityPose)) continue;
                    passenger.setPose(entityPose);
                    return vec3d2;
                }
            }
        }
        return super.getDismountLocationForPassenger(passenger);
    }

    protected void copyEntityData(Entity entity) {
        entity.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
        float g = Mth.clamp(f, -105.0f, 105.0f);
        entity.yRotO += g - f;
        entity.setYRot(entity.getYRot() + g - f);
        entity.setYHeadRot(entity.getYRot());
    }

    @Override
    public void onPassengerTurned(Entity passenger) {
        this.copyEntityData(passenger);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }
        if (this.ticksUnderwater < 60.0f) {
            if (!this.level.isClientSide) {
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void checkFallDamage(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
        this.fallVelocity = this.getDeltaMovement().y;
        if (this.isPassenger()) {
            return;
        }
        if (onGround) {
            if (this.fallDistance > 3.0f) {
                if (this.location != Location.ON_LAND) {
                    this.resetFallDistance();
                    return;
                }
                this.causeFallDamage(this.fallDistance, 1.0f, DamageSource.FALL);
            }
            this.resetFallDistance();
        } else if (!this.level.getFluidState(this.blockPosition().below()).is(FluidTags.WATER) && heightDifference < 0.0) {
            this.fallDistance = (float) ((double) this.fallDistance - heightDifference);
        }
    }

    public float getDamageWobbleStrength() {
        return this.entityData.get(DAMAGE_WOBBLE_STRENGTH).floatValue();
    }

    public void setDamageWobbleStrength(float wobbleStrength) {
        this.entityData.set(DAMAGE_WOBBLE_STRENGTH, Float.valueOf(wobbleStrength));
    }

    public int getDamageWobbleTicks() {
        return this.entityData.get(DAMAGE_WOBBLE_TICKS);
    }

    public void setDamageWobbleTicks(int wobbleTicks) {
        this.entityData.set(DAMAGE_WOBBLE_TICKS, wobbleTicks);
    }

    public int getDamageWobbleSide() {
        return this.entityData.get(DAMAGE_WOBBLE_SIDE);
    }

    public void setDamageWobbleSide(int side) {
        this.entityData.set(DAMAGE_WOBBLE_SIDE, side);
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < 2;
    }

    @Override
    @Nullable
    public Entity getControllingPassenger() {
        return this.getFirstPassenger();
    }

    public void setInputs(boolean pressingLeft, boolean pressingRight, boolean pressingForward, boolean pressingBack) {
        this.pressingLeft = pressingLeft;
        this.pressingRight = pressingRight;
        this.pressingForward = pressingForward;
        this.pressingBack = pressingBack;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public boolean isUnderWater() {
        return this.location == Location.UNDER_WATER || this.location == Location.UNDER_FLOWING_WATER;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(this.asItem());
    }

    public static enum Location {
        IN_WATER,
        UNDER_WATER,
        UNDER_FLOWING_WATER,
        ON_LAND,
        IN_AIR;
    }
}

