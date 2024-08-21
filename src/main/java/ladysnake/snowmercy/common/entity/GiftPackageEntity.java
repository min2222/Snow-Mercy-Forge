package ladysnake.snowmercy.common.entity;

import java.util.Random;

import ladysnake.snowmercy.common.init.SnowMercyGifts;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class GiftPackageEntity extends Entity {
    private static final EntityDataAccessor<Boolean> PARACHUTE = SynchedEntityData.defineId(GiftPackageEntity.class, EntityDataSerializers.BOOLEAN);

    public GiftPackageEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(PARACHUTE, true);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("Parachute")) {
            this.setParachute(nbt.getBoolean("Parachute"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putBoolean("Parachute", this.hasParachute());
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean hasParachute() {
        return this.entityData.get(PARACHUTE);
    }

    public void setParachute(boolean parachute) {
        this.entityData.set(PARACHUTE, parachute);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getYRot() == 0) {
            this.setYRot(new Random().nextFloat() * 360f);
        }

        if (level.isClientSide && !this.hasParachute()) {
            level.addParticle(ParticleTypes.END_ROD, this.getX() + random.nextGaussian() / 5f, this.getY() + random.nextFloat() / 1.5f, this.getZ() + random.nextGaussian() / 5f, 0, 0, 0);
        }

        if (!level.isClientSide) {
            if (this.isOnGround()) {
                this.setParachute(false);
            }

            if (this.hasParachute() && !isOnGround()) {
                this.setDeltaMovement(0, -0.2, 0);
            }
            if (!this.hasParachute() && !isOnGround()) {
                this.setDeltaMovement(0, this.getDeltaMovement().y - 0.05, 0);
            }
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        this.playSound(SoundEvents.FIREWORK_ROCKET_BLAST, 1.0f, 1.5f);
        for (int i = 0; i < 50; i++) {
        	level.addParticle(ParticleTypes.END_ROD, this.getX(), this.getY() + .25, this.getZ(), random.nextGaussian(), random.nextFloat(), random.nextGaussian());
            level.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY() + .25, this.getZ(), random.nextGaussian(), random.nextFloat(), random.nextGaussian());
        }

        for (int i = 0; i < 3; i++) {
            ItemEntity giftItem = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), SnowMercyGifts.getRandomGift(), random.nextGaussian() / 10f, 0.3f, random.nextGaussian() / 10f);
            level.addFreshEntity(giftItem);
        }

        this.discard();

        return InteractionResult.SUCCESS;
    }
}
