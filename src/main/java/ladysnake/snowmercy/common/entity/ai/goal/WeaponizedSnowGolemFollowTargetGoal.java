package ladysnake.snowmercy.common.entity.ai.goal;

import java.util.function.Predicate;

import ladysnake.snowmercy.common.entity.WeaponizedSnowGolemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class WeaponizedSnowGolemFollowTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
    public final int requiredHead;

    public WeaponizedSnowGolemFollowTargetGoal(WeaponizedSnowGolemEntity mob, Class targetClass, boolean checkVisibility, int requiredHead) {
        super(mob, targetClass, checkVisibility);
        this.requiredHead = requiredHead;
    }

    public WeaponizedSnowGolemFollowTargetGoal(WeaponizedSnowGolemEntity mob, Class targetClass, boolean checkVisibility, boolean checkCanNavigate, int requiredHead) {
        super(mob, targetClass, checkVisibility, checkCanNavigate);
        this.requiredHead = requiredHead;
    }

    public WeaponizedSnowGolemFollowTargetGoal(WeaponizedSnowGolemEntity mob, Class targetClass, int reciprocalChance, boolean checkVisibility, boolean checkCanNavigate, int requiredHead, Predicate<LivingEntity> targetPredicate) {
        super(mob, targetClass, reciprocalChance, checkVisibility, checkCanNavigate, targetPredicate);
        this.requiredHead = requiredHead;
    }

    @Override
    public boolean canUse() {
        boolean hasCorrectHead = this.requiredHead == ((WeaponizedSnowGolemEntity) mob).getHead();

        return super.canUse() && hasCorrectHead && targetConditions.test(mob, target);
    }

    @Override
    public boolean canContinueToUse() {
        boolean hasCorrectHead = this.requiredHead == ((WeaponizedSnowGolemEntity) mob).getHead();

        return super.canContinueToUse() && hasCorrectHead && targetConditions.test(mob, target);
    }
}
