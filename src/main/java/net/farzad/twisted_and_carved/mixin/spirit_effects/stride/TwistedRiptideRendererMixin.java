package net.farzad.twisted_and_carved.mixin.spirit_effects.stride;

import com.mojang.blaze3d.vertex.PoseStack;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.init.TCTags;
import net.farzad.twisted_and_carved.common.util.interfaces.StrideRenderStateAddon;
import net.minecraft.client.model.effects.SpinAttackEffectModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpinAttackEffectLayer.class)
public class TwistedRiptideRendererMixin {

    @Unique
    private static Identifier twisted_and_carved$STRIDE_DASH_TEXTURE = TwistedAndCarved.id("textures/entity/twisted_riptide.png");

    @Shadow
    @Final
    private SpinAttackEffectModel model;

    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    private void twisted_and_carved$causeTwistedRiptide(PoseStack poseStack, SubmitNodeCollector queue, int i, AvatarRenderState state, float f, float g, CallbackInfo ci) {
        ItemStack stack;

        if (state instanceof StrideRenderStateAddon renderStateAddon) {
            stack = renderStateAddon.twistedAndCarved$getRiptideStack();
            boolean strideDash = stack.is(TCTags.Items.TWISTED_TOOL);

            if (state.isAutoSpinAttack && strideDash) {
                this.model.setupAnim(state);
                queue.submitModel(this.model, state, poseStack, twisted_and_carved$STRIDE_DASH_TEXTURE, i, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
                ci.cancel();
            }
        }
    }

}
