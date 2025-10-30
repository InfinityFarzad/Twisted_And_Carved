package net.farzad.twisted_and_carved.mixin;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.farzad.twisted_and_carved.common.util.interfaces.TwistedRiptideMixinInterface;
import net.farzad.twisted_and_carved.common.util.interfaces.TwistedRiptideRenderState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.TridentRiptideFeatureRenderer;
import net.minecraft.client.render.entity.model.TridentRiptideEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
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

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/PlayerEntityRenderState;FF)V", at = @At("TAIL"))
    private void twisted_and_carved$causeTwistedRiptide(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, PlayerEntityRenderState playerEntityRenderState, float f, float g, CallbackInfo ci) throws NoSuchFieldException {
        PlayerEntity player = ((((TwistedRiptideRenderState)playerEntityRenderState).twistedAndCarved$getPlayer()));
        ItemStack stack = ((TwistedRiptideMixinInterface)player).twistedAndCarved$getRiptideStack();
        boolean shouldIUseThisCustomRiptide = (stack != null) && stack.isOf(ModItems.TWISTED_GREATAXE) && TwistedWeaponUtil.getAbilityID(stack) == "stride";
        if (playerEntityRenderState.usingRiptide && shouldIUseThisCustomRiptide) {
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(TEXTURE2));
            this.model.setAngles(playerEntityRenderState);
            this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        }
    }

}
