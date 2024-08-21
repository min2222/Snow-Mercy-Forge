package ladysnake.snowmercy.common.entity.ai.goal;

import java.util.EnumSet;

import ladysnake.snowmercy.common.entity.HeadmasterEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class HeadmasterMinigunAttackGoal extends Goal {
    private final HeadmasterEntity headmaster;
    private final HeadmasterEntity owner;
    private final double mobSpeed;
    private final int minIntervalTicks;
    private final int maxIntervalTicks;
    private final float maxShootRange;
    private final float squaredMaxShootRange;
    private final int maxSalvo;
    private final int cooldownBetweenShots;
    private LivingEntity target;
    private int updateCountdownTicks;
    private int seenTargetTicks;
    private int currentSalvo;
    private int currentCooldownBetweenShots;

    public HeadmasterMinigunAttackGoal(HeadmasterEntity mob, double mobSpeed, int intervalTicks, float maxShootRange, int maxSalvo, int cooldownBetweenShots) {
        this(mob, mobSpeed, intervalTicks, intervalTicks, maxShootRange, maxSalvo, cooldownBetweenShots);
    }

    public HeadmasterMinigunAttackGoal(HeadmasterEntity mob, double mobSpeed, int minIntervalTicks, int maxIntervalTicks, float maxShootRange, int maxSalvo, int cooldownBetweenShots) {
        this.updateCountdownTicks = -1;
        if (!(mob instanceof LivingEntity)) {
            throw new IllegalArgumentException("SalvoProjectileAttackGoal requires Mob implements HeadmasterEntity");
        } else {
            this.owner = mob;
            this.headmaster = mob;
            this.mobSpeed = mobSpeed;
            this.minIntervalTicks = minIntervalTicks;
            this.maxIntervalTicks = maxIntervalTicks;
            this.maxShootRange = maxShootRange;
            this.squaredMaxShootRange = maxShootRange * maxShootRange;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.maxSalvo = maxSalvo;
            this.cooldownBetweenShots = cooldownBetweenShots;
        }
    }

    @Override
    public boolean canUse() {
        if (this.headmaster.isTurret()) {
            LivingEntity livingEntity = this.headmaster.getTarget();
            if (livingEntity != null && livingEntity.isAlive()) {
                this.target = livingEntity;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() || !this.headmaster.getNavigation().isDone();
    }

    @Override
    public void stop() {
        this.target = null;
        this.seenTargetTicks = 0;
        this.updateCountdownTicks = -1;
    }

    @Override
    public void tick() {
        double d = this.headmaster.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean bl = this.headmaster.getSensing().hasLineOfSight(this.target);
        if (bl) {
            ++this.seenTargetTicks;
        } else {
            this.seenTargetTicks = 0;
        }

        if (d <= (double) this.squaredMaxShootRange && this.seenTargetTicks >= 5) {
            this.headmaster.getNavigation().stop();
        } else {
            this.headmaster.getNavigation().moveTo(this.target, this.mobSpeed);
        }

        this.headmaster.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        float f;
        if (--this.updateCountdownTicks == 0) {
            if (!bl) {
                return;
            }
            
            f = Mth.sqrt((float) d) / this.maxShootRange;
            this.currentCooldownBetweenShots = this.cooldownBetweenShots;
            this.currentSalvo = 0;
            this.updateCountdownTicks = Mth.floor(f * (float) (this.maxIntervalTicks - this.minIntervalTicks) + (float) this.minIntervalTicks);
        } else if (this.updateCountdownTicks < 0) {
            f = Mth.sqrt((float) d) / this.maxShootRange;
            this.currentSalvo = 0;
            this.updateCountdownTicks = Mth.floor(f * (float) (this.maxIntervalTicks - this.minIntervalTicks) + (float) this.minIntervalTicks);
        }

        if (this.currentSalvo < this.maxSalvo) {
            if (this.currentCooldownBetweenShots-- <= 0) {
                f = Mth.sqrt((float) d) / this.maxShootRange;
                float g = Mth.clamp(f, 0.1F, 1.0F);
                this.owner.attack(this.target, g);
                this.currentCooldownBetweenShots = this.cooldownBetweenShots;
                this.currentSalvo++;
            }
        }
    }
}
