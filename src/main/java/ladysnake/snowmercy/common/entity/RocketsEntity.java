package ladysnake.snowmercy.common.entity;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import ladysnake.snowmercy.common.entity.ai.goal.SalvoProjectileAttackGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RocketsEntity extends WeaponizedSnowGolemEntity implements RangedAttackMob {
    public static final ItemStack FIREWORKS = new ItemStack(Items.FIREWORK_ROCKET, 1);

    public RocketsEntity(EntityType<RocketsEntity> entityType, Level world) {
        super(entityType, world);

        FIREWORKS.getOrCreateTagElement("Fireworks").putInt("Flight", 1);

        ListTag explosions = new ListTag();
        CompoundTag explosion = new CompoundTag();
        explosion.putBoolean("Flicker", false);
        explosion.putBoolean("Trail", true);
        explosion.putInt("Type", 0);
        explosion.putIntArray("Colors", new int[]{15790320});
        explosions.add(explosion);
        FIREWORKS.getOrCreateTagElement("Fireworks").put("Explosions", explosions);

        ItemStack stackInHand = FIREWORKS.copy();
        this.setItemSlot(EquipmentSlot.MAINHAND, stackInHand);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.25F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SalvoProjectileAttackGoal(this, 1.25D, 120, 16f, 1, 3));
    }

    @Override
    public WeaponizedGolemType getGolemType() {
        return WeaponizedGolemType.ROCKETS;
    }

    public void performRangedAttack(LivingEntity target, float pullProgress) {
        FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(level, FIREWORKS, this, this.getX(), this.getEyeY() - 0.15000000596046448D, this.getZ(), true);
        Vec3 vec3d = this.getUpVector(1.0F);
        Quaternion quaternion = new Quaternion(new Vector3f(vec3d), 0f, true);
        Vec3 vec3d2 = this.getViewVector(1.0F);
        Vector3f vector3f = new Vector3f(vec3d2);
        vector3f.transform(quaternion);
        fireworkRocketEntity.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 3f, 5f);
        level.addFreshEntity(fireworkRocketEntity);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        ItemStack droppedStack = this.getMainHandItem();
        droppedStack.setCount(random.nextInt(6 * (lootingMultiplier + 1)));
        this.spawnAtLocation(droppedStack);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
//        if (this.isAlive() && (source == DamageSource.ON_FIRE || source == DamageSource.IN_FIRE)) {
//            FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(world, FIREWORKS, this, this.getX() + random.nextGaussian() / 10f, this.getEyeY() + random.nextGaussian() / 10f, this.getZ() + random.nextGaussian() / 10f, true);
//            fireworkRocketEntity.setVelocity(0, 0, 0, 0, 0);
//            world.spawnEntity(fireworkRocketEntity);
//            ((FireworkRocketEntityInvoker) fireworkRocketEntity).invokeExplodeAndRemove();
//            this.setInvisible(true);
//            this.kill();
//        }

        return super.hurt(source, amount);
    }
}
