package net.farzad.twisted_and_carved.mixin.spirit_effects.stride;

import net.akws.chiseled_lib.common.util.EnchantmentUtil;
import net.farzad.twisted_and_carved.common.util.interfaces.StrideRenderStateAddon;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class PlayerEntityRendererMixin <AvatarlikeEntity extends Avatar & ClientAvatarEntity> {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    private void twisted_and_carved$updatePlayerRiptideState(AvatarlikeEntity playerLikeEntity, AvatarRenderState playerEntityRenderState, float f, CallbackInfo ci) {
        if (playerEntityRenderState instanceof StrideRenderStateAddon renderStateAddon) {
            if (playerLikeEntity instanceof Player player) {
                renderStateAddon.twistedAndCarved$setRiptideStack(EnchantmentUtil.getRiptideStack(player));
            }
        }
    }

}
