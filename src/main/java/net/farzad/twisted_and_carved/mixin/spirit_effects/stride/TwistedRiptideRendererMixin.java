package net.farzad.twisted_and_carved.mixin.spirit_effects.stride;

import com.mojang.blaze3d.vertex.PoseStack;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.farzad.twisted_and_carved.common.util.interfaces.TwistedRiptideRenderState;
import net.minecraft.client.model.effects.SpinAttackEffectModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
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
    Identifier TEXTURE2 = Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "textures/entity/twisted_riptide.png");

    @Shadow
    @Final
    private SpinAttackEffectModel model;

    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V", at = @At("TAIL"))
    private void twisted_and_carved$causeTwistedRiptide(PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, int i, AvatarRenderState playerEntityRenderState, float f, float g, CallbackInfo ci) throws NoSuchFieldException {
        ItemStack stack = ((TwistedRiptideRenderState)playerEntityRenderState).twistedAndCarved$getRiptideStack();
        boolean shouldIUseThisCustomRiptide = stack != null && stack.is(TCItems.TWISTED_GREATAXE);
        if (playerEntityRenderState.isAutoSpinAttack && shouldIUseThisCustomRiptide) {
            this.model.setupAnim(playerEntityRenderState);
            orderedRenderCommandQueue.submitModel(this.model, playerEntityRenderState, matrixStack, this.model.renderType(TEXTURE2), i, OverlayTexture.NO_OVERLAY, playerEntityRenderState.outlineColor, (ModelFeatureRenderer.CrumblingOverlay)null);
        }
    }

}
