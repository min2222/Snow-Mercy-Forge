package ladysnake.snowmercy.common.entity.ai.goal;

import java.util.Random;

import ladysnake.snowmercy.common.entity.HeadmasterEntity;
import ladysnake.snowmercy.common.entity.SnowGolemHeadEntity;
import ladysnake.snowmercy.common.entity.WeaponizedSnowGolemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class ReviveSurgeGoal extends Goal {
    protected HeadmasterEntity headmaster;
    protected int ticksUntilSurge;
    protected Random random;

    public ReviveSurgeGoal(HeadmasterEntity headmasterEntity) {
        this.headmaster = headmasterEntity;
    }

    @Override
    public boolean canUse() {
        // if snowmen heads around
        return !headmaster.level.getEntitiesOfClass(SnowGolemHeadEntity.class, this.headmaster.getBoundingBox().inflate(16f), snowGolemHeadEntity -> snowGolemHeadEntity.isAlive() && snowGolemHeadEntity.onGround()).isEmpty();
    }

    @Override
    public void start() {
        super.start();

        random = new Random();
        this.ticksUntilSurge = 40;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }

    @Override
    public void tick() {
        super.tick();

        if (ticksUntilSurge == 40) {
            headmaster.playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE, 5.0f, 0.5f);
        } else if (ticksUntilSurge > 0) {
            ((ServerLevel) headmaster.level).sendParticles(ParticleTypes.SOUL, headmaster.getX(), headmaster.getY() + .25f, headmaster.getZ(), 50, random.nextGaussian() * 16f, 0, random.nextGaussian() * 16f, 0f);
        } else {
            headmaster.playSound(SoundEvents.TOTEM_USE, 5.0f, 1.5f);
            headmaster.playSound(SoundEvents.PLAYER_HURT_FREEZE, 5.0f, 1.0f);

            if (!headmaster.level.isClientSide) {
                for (SnowGolemHeadEntity snowmanHead : headmaster.level.getEntitiesOfClass(SnowGolemHeadEntity.class, this.headmaster.getBoundingBox().inflate(16f), LivingEntity::isAlive)) {
                    ((ServerLevel) headmaster.level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Blocks.SNOW_BLOCK)), snowmanHead.getX(), snowmanHead.getY(), snowmanHead.getZ(), 100, random.nextGaussian() / 3f, random.nextGaussian() + 1 / 3f, random.nextGaussian() / 3f, 0.2f);
                    ((ServerLevel) headmaster.level).sendParticles(ParticleTypes.TOTEM_OF_UNDYING, snowmanHead.getX(), snowmanHead.getY(), snowmanHead.getZ(), 50, random.nextGaussian() / 3f, random.nextGaussian() + 1 / 3f, random.nextGaussian() / 3f, 0.2f);
                    snowmanHead.hurt(snowmanHead.damageSources().generic(), 1.0f);
                    WeaponizedSnowGolemEntity golem = snowmanHead.getGolemType().getEntityType().create(headmaster.level);
                    BlockPos blockPos = snowmanHead.blockPosition().offset(0, 0, 0);
                    assert golem != null;
                    golem.moveTo((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.05D, (double) blockPos.getZ() + 0.5D, 0.0F, 0.0F);
                    headmaster.level.addFreshEntity(golem);
                }

//                for (LivingEntity livingEntity : headmaster.world.getEntitiesByClass(LivingEntity.class, this.headmaster.getBoundingBox().expand(16f), LivingEntity::canFreeze)) {
//                    livingEntity.setFrozenTicks(1000);
//                }
            }

            this.stop();
        }

        ticksUntilSurge--;
    }
}
