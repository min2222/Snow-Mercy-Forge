package ladysnake.snowmercy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import ladysnake.snowmercy.common.entity.SledgeEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;

@Mixin(LocalPlayer.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayer {
    @Shadow
    public Input input;

    @Shadow
    public boolean handsBusy;

    public ClientPlayerEntityMixin(ClientLevel p_234112_, GameProfile p_234113_) {
    	super(p_234112_, p_234113_);
    }

    @Inject(method = "rideTick", at = @At("TAIL"))
    private void rideTick(CallbackInfo callbackInfo) {
        if (this.getVehicle() instanceof SledgeEntity sledgeEntity) {
            sledgeEntity.setInputs(this.input.left, this.input.right, this.input.up, this.input.down);
            this.handsBusy |= this.input.left || this.input.right || this.input.up || this.input.down;
        }
    }
}
