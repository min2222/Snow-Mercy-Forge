package ladysnake.snowmercy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ladysnake.snowmercy.common.init.SnowMercyItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

@Mixin(PlayerRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "getArmPose", at = @At("RETURN"), cancellable = true)
    private static void getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> callbackInfoReturnable) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (itemStack.getItem() == SnowMercyItems.COAL_BURNER.get()) {
            callbackInfoReturnable.setReturnValue(HumanoidModel.ArmPose.BOW_AND_ARROW);
        }
    }
}