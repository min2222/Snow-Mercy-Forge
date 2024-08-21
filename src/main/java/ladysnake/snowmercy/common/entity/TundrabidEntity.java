package ladysnake.snowmercy.common.entity;

import ladysnake.snowmercy.common.entity.ai.goal.GoToHeartGoal;
import ladysnake.snowmercy.mixin.FoxEntityInvoker;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TundrabidEntity extends Fox implements SnowMercyEnemy {
    public TundrabidEntity(EntityType<? extends Fox> entityType, Level world) {
        super(entityType, world);

        ((FoxEntityInvoker) this).invokeSetType(Type.SNOW);
    }

    public static AttributeSupplier.Builder createTundrabidAttributes() {
        return Fox.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.3f).add(Attributes.FOLLOW_RANGE, 64.0f);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.targetSelector.addGoal(1, new GoToHeartGoal(this, 1.0f, false, 20));
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean isFreezing() {
        return false;
    }

    @Override
    protected void tryAddFrost() {
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return target instanceof Player || target instanceof WeaponizedSnowGolemEntity;
    }
}
