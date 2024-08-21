package ladysnake.snowmercy.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ladysnake.snowmercy.cca.SnowMercyComponents;
import ladysnake.snowmercy.common.world.ExtendedBiome;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

@Mixin(Biome.class)
public class FrozenTemperatureMixin implements ExtendedBiome {
    @Unique
    private Level world;
    
    @Inject(at = @At(value = "RETURN"), method = "getBaseTemperature()F", cancellable = true)
    private void getTemperature(CallbackInfoReturnable<Float> cir) {
        if (this.world != null && SnowMercyComponents.SNOWMERCY.isEventOngoing()) {
            cir.setReturnValue(-10f);
        }
    }

    @Inject(at = @At(value = "RETURN"), method = "getPrecipitationAt", cancellable = true)
    private void getPrecipitationAt(BlockPos pos, CallbackInfoReturnable<Biome.Precipitation> cir) {
        if (this.world != null && SnowMercyComponents.SNOWMERCY.isEventOngoing() && cir.getReturnValue() == Biome.Precipitation.RAIN) {
            cir.setReturnValue(Biome.Precipitation.SNOW);
        }
    }

    @Override
    public void frostlegion_setWorld(@Nullable Level world) {
        this.world = world;
    }
}
