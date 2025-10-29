package net.farzad.twisted_and_carved.mixin;

import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.farzad.twisted_and_carved.common.item.custom.TwistedToolItem;
import net.farzad.twisted_and_carved.common.util.ModTags;
import net.farzad.twisted_and_carved.common.util.interfaces.TwistedGlintInterface;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawingContextMixin implements TwistedGlintInterface {

    @Inject(method = "drawItemBar", at = @At("TAIL"))
    private void twistedAndCarved$drawItemBar(ItemStack stack, int x, int y, CallbackInfo ci) {
        DrawContext drawContext = (DrawContext) (Object) this;
        if (stack.isItemBarVisible() && stack.isOf(ModItems.TWISTED_GREATAXE)) {
            int i = x + 2;
            int j = y + 13;
            int halfPoint = (13 / 2);

            drawContext.fill(RenderLayer.getGui(), i, j, i + 13, j + 2, 200, -16777216);
            if (stack.getItemBarStep() <= halfPoint) {
                drawContext.fill(RenderLayer.getGui(), i, j, i + stack.getItemBarStep(), j + 1, 200, ColorHelper.fullAlpha(stack.getItemBarColor()));

            } else if (stack.getItemBarStep() > halfPoint) {
                drawContext.fill(RenderLayer.getGui(), i, j, i + 6, j + 1, 200, ColorHelper.fullAlpha(stack.getItemBarColor()));
                drawContext.fill(RenderLayer.getGui(), i + 7, j, i + stack.getItemBarStep(), j + 1, 200, ColorHelper.fullAlpha(stack.getItemBarColor()));
            }
        }
    }

    @Inject(method = "drawStackOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.AFTER))
    private void twisted_and_carved$drawStackGlint(TextRenderer textRenderer, ItemStack stack, int x, int y, String stackCountText, CallbackInfo ci) {
        twistedAndCarved$drawItemGlint(stack,x,y);
    }

    @Override
    public void twistedAndCarved$drawItemGlint(ItemStack item, int x, int y) {
        if (!item.isEmpty() && item.isIn(ModTags.Items.TWISTED_TOOL) && TwistedToolItem.hasAura(item.getOrDefault(ModDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY))) {
            DrawContext drawContext = (DrawContext) (Object) this;
            drawContext.fillGradient(RenderLayer.getGui(),x,y,x +16, y + 8,ColorHelper.withAlpha(2,16711693),  ColorHelper.withAlpha(45,12779591),200);
            drawContext.fillGradient(RenderLayer.getGui(),x,y + 8,x +16, y + 16,ColorHelper.withAlpha(45,12779591),  ColorHelper.withAlpha(2,16711693),200);

        }
    }

}
