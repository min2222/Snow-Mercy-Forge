package ladysnake.snowmercy.common.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;

public class MortarsEntity extends WeaponizedSnowGolemEntity implements RangedAttackMob {
    public MortarsEntity(EntityType<MortarsEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25D, 80, 40f));
    }

    @Override
    public WeaponizedGolemType getGolemType() {
        return WeaponizedGolemType.MORTARS;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress) {
        level.playSound(null, this.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.HOSTILE, 1.0f, 0.6f);
        for (int i = 0; i < 20; i++) {
            IcicleEntity entity = new IcicleEntity(level, this);
            entity.setPos(this.getX(), this.getY(), this.getZ());
            entity.syncPacketPositionCodec(this.getX(), this.getY() + 0.5f, this.getZ());
            entity.setDeltaMovement((target.getX() - this.getX()) / 50f + random.nextGaussian() / 10f, 2f + random.nextGaussian() / 10f, (target.getZ() - this.getZ()) / 50f + random.nextGaussian() / 10f);
            level.addFreshEntity(entity);
        }
    }
}
