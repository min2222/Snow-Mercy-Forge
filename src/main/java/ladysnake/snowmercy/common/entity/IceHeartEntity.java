package ladysnake.snowmercy.common.entity;

import java.util.List;

import ladysnake.snowmercy.cca.SnowMercyComponents;
import ladysnake.snowmercy.common.init.SnowMercyBlocks;
import ladysnake.snowmercy.common.init.SnowMercyEntities;
import ladysnake.snowmercy.common.init.SnowMercySoundEvents;
import ladysnake.snowmercy.common.init.SnowMercyWaves;
import ladysnake.snowmercy.common.init.WaveSpawnEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.ForgeEventFactory;
	
public class IceHeartEntity extends Entity {
    public static final int SPAWN_RADIUS = 100;
    public static final int GIFT_SPAWN_RADIUS = 20;
    private static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(IceHeartEntity.class, EntityDataSerializers.BOOLEAN);

    public IceHeartEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ACTIVE, false);
    }	

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("Active")) {
            this.setActive(nbt.getBoolean("Active"));
        }
        // some tweaks;
        if(nbt.contains("SnowMercyOngoing")) {
            SnowMercyComponents.SNOWMERCY.isEventOngoing = nbt.getBoolean("SnowMercyOngoing");
        }
        
        if(nbt.contains("SnowMercyWave")) {
        	SnowMercyComponents.SNOWMERCY.setEventWave(nbt.getInt("SnowMercyWave"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putBoolean("Active", this.isActive());
        // some tweaks;
        nbt.putBoolean("SnowMercyOngoing", SnowMercyComponents.SNOWMERCY.isEventOngoing());
        nbt.putInt("SnowMercyWave", SnowMercyComponents.SNOWMERCY.getEventWave());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.isActive() && !level.isClientSide) {
            this.playSound(SoundEvents.END_PORTAL_SPAWN, 10.0f, 2.0f);
            this.setActive(true);
            
            //TODO
            int wave = SnowMercyComponents.SNOWMERCY.getEventWave();
            level.players().forEach(serverPlayerEntity -> {
                MutableComponent waveText = Component.translatable("info.snowmercy.wave_start", level.dimension().registry().getPath()).append(wave + 1 + ": ");
                int i = 0;
                for (WaveSpawnEntry waveSpawnEntry : SnowMercyWaves.WAVES.get(wave)) {
                    if (i > 0) {
                        waveText.append(", ");
                    }
                    waveText.append(waveSpawnEntry.entityType.getDescription());
                    i++;
                }

                serverPlayerEntity.displayClientMessage(waveText.setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
                serverPlayerEntity.playNotifySound(SoundEvents.END_PORTAL_SPAWN, SoundSource.MASTER, 1.0f, 2.0f);
            });
        }

        return super.hurt(source, amount);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        this.tickCount++;

        if (level.getGameTime() % 65 == 0) {
            this.playSound(SnowMercySoundEvents.HEART_OF_ICE_AMBIENT.get(), 1.0f, 1.0f);
        }

        if (this.isActive()) {
            this.level.addParticle(ParticleTypes.SNOWFLAKE, this.getX(), this.getY() + .25, this.getZ(), random.nextGaussian() / 10f, random.nextGaussian() / 10f, random.nextGaussian() / 10f);
            this.level.addParticle(ParticleTypes.END_ROD, this.getX(), this.getY() + .25, this.getZ(), random.nextGaussian() / 10f, random.nextGaussian() / 10f, random.nextGaussian() / 10f);

            if (!level.isClientSide && this.tickCount % 5 == 0) {
            	//TODO
                int wave = SnowMercyComponents.SNOWMERCY.getEventWave();
                // check if there are enemies left to spawn
                List<Mob> enemiesLeft = level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(100f, 30f, 100f), entity -> entity instanceof SnowMercyEnemy);
                SnowMercyWaves.WAVES.get(wave).removeIf(waveSpawnEntry -> waveSpawnEntry.count <= 0);
                if (!SnowMercyWaves.WAVES.get(wave).isEmpty() && enemiesLeft.size() < 50) {
                    int i = random.nextInt(SnowMercyWaves.WAVES.get(wave).size());

                    Mob enemy = SnowMercyWaves.WAVES.get(wave).get(i).entityType.create(this.level);

                    ForgeEventFactory.onFinalizeSpawn(enemy, (ServerLevelAccessor) level, level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);

                    var angle = Math.random() * Math.PI * 2;
                    float x = (float) (this.getX() + (Math.cos(angle) * SPAWN_RADIUS));
                    float z = (float) (this.getZ() + (Math.sin(angle) * SPAWN_RADIUS));

                    if (enemy instanceof IceballEntity) {
                        x = (float) (this.getX() + (Math.cos(angle) * (SPAWN_RADIUS / 5f)));
                        z = (float) (this.getZ() + (Math.sin(angle) * (SPAWN_RADIUS / 5f)));
                    }

                    BlockPos offsetPos = BlockPos.containing(x, this.getY(), z);
                    for (int groundOffset = -10; groundOffset < 10; groundOffset++) {
                        if ((level.getBlockState(offsetPos.offset(0, groundOffset, 0)).isAir() || level.getBlockState(offsetPos.offset(0, groundOffset, 0)).getBlock() == Blocks.SNOW) && level.getBrightness(LightLayer.SKY, offsetPos.offset(0, groundOffset, 0)) >= 15f && (level.getBlockState(offsetPos.offset(0, groundOffset - 1, 0)).isRedstoneConductor(level, offsetPos.offset(0, groundOffset - 1, 0)) || level.getBlockState(offsetPos.offset(0, groundOffset - 1, 0)).getBlock() == Blocks.ICE)) {
                            enemy.setPos(offsetPos.getX(), offsetPos.getY() + groundOffset, offsetPos.getZ());
                            enemy.setPersistenceRequired();
                            level.addFreshEntity(enemy);
                            SnowMercyWaves.WAVES.get(wave).get(i).count--;
                            break;
                        }
                    }

                } else {
                    // if there are no ennemies left to spawn, check if all have been defeated
                    if (enemiesLeft.size() <= 10) {
                        for (int i = 0; i < wave + 1; i++) {
                            double r = GIFT_SPAWN_RADIUS * Math.sqrt(random.nextFloat());
                            double theta = random.nextFloat() * 2 * Math.PI;

                            GiftPackageEntity giftPackageEntity = new GiftPackageEntity(SnowMercyEntities.GIFT_PACKAGE.get(), level);
                            giftPackageEntity.setPos(this.getX() + r * Math.cos(theta), this.getY() + 60, this.getZ() + r * Math.sin(theta));
                            level.addFreshEntity(giftPackageEntity);
                        }
                        
                        //TODO
                        SnowMercyComponents.SNOWMERCY.setEventWave(wave + 1);

                        level.players().forEach(serverPlayerEntity -> {
                            serverPlayerEntity.displayClientMessage(
                                    Component.translatable("info.snowmercy.wave_cleared", level.dimension().registry().getPath()).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
                        });

//                        if (SnowMercyComponents.SNOWMERCY.get(world).getEventWave() >= 9) {
                        level.setBlockAndUpdate(this.blockPosition(), SnowMercyBlocks.FROZEN_LODESTONE.get().defaultBlockState());
//                        }

                        this.discard();
                        this.playSound(SoundEvents.PLAYER_HURT_FREEZE, 1.0f, 1.2f);
                        this.playSound(SoundEvents.GLASS_BREAK, 1.0f, 1.2f);
                        ((ServerLevel) this.level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.PACKED_ICE, 1)), this.getX(), this.getY() + .25, this.getZ(), 200, 0, 0, 0, random.nextGaussian() / 5f);
                    } else {
                        for (Mob mobEntity : enemiesLeft) {
                            if (Math.sqrt(this.distanceToSqr(mobEntity)) > 80f && mobEntity.isAlive()) {
                                mobEntity.setGlowingTag(true);
                            } else {
                                mobEntity.setGlowingTag(false);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isActive() {
        return this.entityData.get(ACTIVE);
    }

    public void setActive(boolean active) {
        this.entityData.set(ACTIVE, active);
    }
}
