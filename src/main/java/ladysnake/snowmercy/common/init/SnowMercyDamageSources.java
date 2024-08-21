package ladysnake.snowmercy.common.init;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class SnowMercyDamageSources {
    public static DamageSource ramming(Entity attacker) {
        return new EntityDamageSource("ramming", attacker).setNoAggro();
    }

    public static DamageSource icicle(AbstractArrow projectile, @Nullable Entity attacker) {
        return (new IndirectEntityDamageSource("icicle", projectile, attacker)).setProjectile();
    }

}
