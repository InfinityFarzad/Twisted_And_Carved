package net.farzad.twisted_and_carved.mixin.spirit_effects.stride;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.farzad.twisted_and_carved.common.util.interfaces.TwistedRiptideRenderState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.TridentRiptideFeatureRenderer;
import net.minecraft.client.render.entity.model.TridentRiptideEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentRiptideFeatureRenderer.class)
public class TwistedRiptideRendererMixin {

    @Unique
    Identifier TEXTURE2 = Identifier.of(TwistedAndCarved.MOD_ID, "textures/entity/twisted_riptide.png");

    @Shadow
    @Final
    private TridentRiptideEntityModel model;

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/PlayerEntityRenderState;FF)V", at = @At("TAIL"))
    private void twisted_and_carved$causeTwistedRiptide(MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, int i, PlayerEntityRenderState playerEntityRenderState, float f, float g, CallbackInfo ci) throws NoSuchFieldException {
        ItemStack stack = ((TwistedRiptideRenderState)playerEntityRenderState).twistedAndCarved$getRiptideStack();
        boolean shouldIUseThisCustomRiptide = stack != null && stack.isOf(TCItems.TWISTED_GREATAXE);
        if (playerEntityRenderState.usingRiptide && shouldIUseThisCustomRiptide) {
            this.model.setAngles(playerEntityRenderState);
            orderedRenderCommandQueue.submitModel(this.model, playerEntityRenderState, matrixStack, this.model.getLayer(TEXTURE2), i, OverlayTexture.DEFAULT_UV, playerEntityRenderState.outlineColor, (ModelCommandRenderer.CrumblingOverlayCommand)null);
        }
    }

}
