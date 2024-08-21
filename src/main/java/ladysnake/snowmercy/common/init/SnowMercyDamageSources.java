package ladysnake.snowmercy.common.init;

import org.jetbrains.annotations.Nullable;

import ladysnake.snowmercy.common.SnowMercy;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class SnowMercyDamageSources {
    public static final ResourceKey<DamageType> RAMMING = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(SnowMercy.MODID, "ramming"));
    public static final ResourceKey<DamageType> ICICLE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(SnowMercy.MODID, "icicle"));
    
    public static DamageSource ramming(Entity attacker) {
        return new DamageSource(attacker.level.registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(RAMMING), attacker);
    }

    public static DamageSource icicle(AbstractArrow projectile, @Nullable Entity attacker) {
        return (new DamageSource(projectile.level.registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(ICICLE), projectile, attacker));
    }

}
