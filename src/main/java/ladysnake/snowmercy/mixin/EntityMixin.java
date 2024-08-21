package ladysnake.snowmercy.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ladysnake.snowmercy.common.entity.SnowMercyEnemy;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.Team;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    @Nullable
    public abstract Team getTeam();

    @Inject(method = "getTeamColor", at = @At("RETURN"), cancellable = true)
    private void getTeamColor(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        if (this instanceof SnowMercyEnemy) {
            callbackInfoReturnable.setReturnValue(0xFF0000);
        }
    }
}
