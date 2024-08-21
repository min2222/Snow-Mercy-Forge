package ladysnake.snowmercy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

@Mixin(Monster.class)
public abstract class HostileEntityMixin extends LivingEntity {
    protected HostileEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "checkMonsterSpawnRules", at = @At("RETURN"), cancellable = true)
    private static void canSpawnInDark(EntityType<? extends Monster> type, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (world.dimensionType().coordinateScale() == 2 && !world.dimensionType().natural() && !world.dimensionType().ultraWarm() && world.dimensionType().bedWorks() && world.dimensionType().hasRaids()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
}
