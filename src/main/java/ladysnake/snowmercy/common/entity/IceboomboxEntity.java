package ladysnake.snowmercy.common.entity;

import ladysnake.snowmercy.common.entity.ai.goal.FollowGoal;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class IceboomboxEntity extends WeaponizedSnowGolemEntity {
    public IceboomboxEntity(EntityType<IceboomboxEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createEntityAttributes() {
        return WeaponizedSnowGolemEntity.createEntityAttributes().add(Attributes.ATTACK_DAMAGE, 0.0d);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FollowGoal(this, 1.0D, false, 4));
    }

    @Override
    public WeaponizedGolemType getGolemType() {
        return WeaponizedGolemType.BOOMBOX;
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide && this.tickCount % 10 == 0) {
        	level.addParticle(ParticleTypes.NOTE, this.getX(), this.getY() + 2.1, this.getZ(), (double) random.nextInt(13) / 24.0, 0.0, 0.0);
        }
    }
}
