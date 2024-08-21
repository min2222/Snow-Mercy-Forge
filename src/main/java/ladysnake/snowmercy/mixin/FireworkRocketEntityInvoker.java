package ladysnake.snowmercy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.projectile.FireworkRocketEntity;

@Mixin(FireworkRocketEntity.class)
public interface FireworkRocketEntityInvoker {
    @Invoker("explode")
    public void invokeExplodeAndRemove();
}
