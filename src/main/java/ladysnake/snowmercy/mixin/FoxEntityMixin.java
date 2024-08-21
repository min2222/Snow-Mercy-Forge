package ladysnake.snowmercy.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ladysnake.snowmercy.common.entity.TundrabidEntity;
import ladysnake.snowmercy.common.entity.WeaponizedSnowGolemEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(Fox.class)
public abstract class FoxEntityMixin extends Animal {
    protected FoxEntityMixin(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo callbackInfo) {
        if ((Object) this instanceof TundrabidEntity) {
            this.level.addParticle(ParticleTypes.SNOWFLAKE, this.getX() - (double) (this.getBbWidth() + 1.0F) * 0.5D * (double) Mth.sin(this.yBodyRot * 0.017453292F), this.getEyeY() - 0.10000000149011612D, this.getZ() + (double) (this.getBbWidth() + 1.0F) * 0.5D * (double) Mth.cos(this.yBodyRot * 0.017453292F), 0, -0.01D, 0);
        }
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    protected void initGoals(CallbackInfo callbackInfo) {
        if ((Object) this instanceof TundrabidEntity) {
            this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, false, false, Entity::isAttackable));
            this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, WeaponizedSnowGolemEntity.class, 10, false, false, livingEntity -> livingEntity instanceof WeaponizedSnowGolemEntity && ((WeaponizedSnowGolemEntity) livingEntity).getHead() != 1));
        }
    }

    @Inject(method = "trusts", at = @At("RETURN"), cancellable = true)
    void canTrust(UUID uuid, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if ((Object) this instanceof TundrabidEntity) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @Inject(method = "isDefending", at = @At("RETURN"), cancellable = true)
    void isAggressive(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if ((Object) this instanceof TundrabidEntity) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @Inject(method = "isSleeping", at = @At("RETURN"), cancellable = true)
    private void isSleeping(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if ((Object) this instanceof TundrabidEntity) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method = "getFoxType", at = @At("RETURN"), cancellable = true)
    private void getFoxType(CallbackInfoReturnable<Fox.Type> callbackInfoReturnable) {
        if ((Object) this instanceof TundrabidEntity) {
            callbackInfoReturnable.setReturnValue(Fox.Type.SNOW);
        }
    }

}