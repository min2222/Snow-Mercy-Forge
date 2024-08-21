package ladysnake.snowmercy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.animal.Fox;

@Mixin(Fox.class)
public interface FoxEntityInvoker {
    @Invoker("setFoxType")
    public void invokeSetType(Fox.Type type);
}
