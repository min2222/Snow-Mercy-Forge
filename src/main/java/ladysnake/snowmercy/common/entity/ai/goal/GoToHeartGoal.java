package ladysnake.snowmercy.common.entity.ai.goal;

import java.util.EnumSet;
import java.util.List;

import ladysnake.snowmercy.common.entity.IceHeartEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

public class GoToHeartGoal extends Goal {
    private final double speed;
    private final boolean pauseWhenMobIdle;
    private final int radius;
    protected Mob mob;
    private Path path;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int updateCountdownTicks;
    private int field_24667;
    private long lastUpdateTime;
    private IceHeartEntity targetHeart;

    public GoToHeartGoal(Mob mob, double speed, boolean pauseWhenMobIdle, int radius) {
        this.mob = mob;
        this.speed = speed;
        this.pauseWhenMobIdle = pauseWhenMobIdle;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.radius = radius;
    }

    @Override
    public boolean canUse() {
        if (!(this.mob.getTarget() instanceof Player)) {
            long l = this.mob.level.getGameTime();
            if (l - this.lastUpdateTime < 20L) {
                return false;
            } else {
                this.lastUpdateTime = l;
                List<IceHeartEntity> activeHearts = this.mob.level.getEntitiesOfClass(IceHeartEntity.class, this.mob.getBoundingBox().inflate(150f, 50f, 150f), IceHeartEntity::isActive);
                if (!activeHearts.isEmpty()) {
                    IceHeartEntity heartEntity = activeHearts.get(0);

                    if (!heartEntity.isAlive()) {
                        return false;
                    } else {
                        this.path = this.mob.getNavigation().createPath(heartEntity, 0);
                        if (this.path != null) {
                            return true;
                        } else {
                            return this.mob.distanceToSqr(heartEntity.getX(), heartEntity.getY(), heartEntity.getZ()) >= radius;
                        }
                    }
                } else {
                    return false;
                }
            }
        } else return false;
    }

    @Override
    public boolean canContinueToUse() {
        return !(this.mob.getTarget() instanceof Player);
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.speed);
        this.updateCountdownTicks = 0;
        this.field_24667 = 0;
        List<IceHeartEntity> activeHearts = this.mob.level.getEntitiesOfClass(IceHeartEntity.class, this.mob.getBoundingBox().inflate(100f, 50f, 100f), IceHeartEntity::isActive);
        if (!activeHearts.isEmpty()) {
            this.targetHeart = activeHearts.get(0);
        } else {
            this.stop();
        }
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.targetHeart != null && targetHeart.isAlive() && targetHeart.isActive()) {
            if (Math.sqrt(this.mob.blockPosition().distSqr(targetHeart.blockPosition())) <= radius) {
                this.stop();
            }

            this.mob.getLookControl().setLookAt(targetHeart, 30.0F, 30.0F);

            assert targetHeart != null;
            double d = this.mob.distanceToSqr(targetHeart.getX(), targetHeart.getY(), targetHeart.getZ());
            this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
            if (this.updateCountdownTicks <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || targetHeart.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.mob.getRandom().nextFloat() < 0.05F)) {
                this.targetX = targetHeart.getX();
                this.targetY = targetHeart.getY();
                this.targetZ = targetHeart.getZ();
                this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);
                if (d > 1024.0D) {
                    this.updateCountdownTicks += 10;
                } else if (d > 256.0D) {
                    this.updateCountdownTicks += 5;
                }

                if (!this.mob.getNavigation().moveTo(targetHeart, this.speed)) {
                    this.updateCountdownTicks += 15;
                }
            }

            this.field_24667 = Math.max(this.field_24667 - 1, 0);
        } else {
            this.stop();
        }
    }
}
