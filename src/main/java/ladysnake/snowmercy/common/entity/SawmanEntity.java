package ladysnake.snowmercy.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.level.Level;

public class SawmanEntity extends WeaponizedSnowGolemEntity {
    public SawmanEntity(EntityType<SawmanEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
    }

    @Override
    public WeaponizedGolemType getGolemType() {
        return WeaponizedGolemType.SAWMAN;
    }
}
