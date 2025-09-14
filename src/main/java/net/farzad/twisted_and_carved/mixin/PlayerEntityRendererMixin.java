package net.farzad.twisted_and_carved.mixin;

import net.farzad.twisted_and_carved.common.util.interfaces.TwistedRiptideRenderState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @Inject(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At("TAIL"))
    private void twisted_and_carved$updatePlayerRiptideState(AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo ci) {
        if (abstractClientPlayerEntity instanceof PlayerEntity player) {
            ((TwistedRiptideRenderState) playerEntityRenderState).twistedAndCarved$setPlayer(player);
        }
    }

}
