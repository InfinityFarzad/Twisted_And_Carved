package net.farzad.twisted_and_carved.mixin.client;

import net.farzad.twisted_and_carved.common.register.TCDataComponents;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.farzad.twisted_and_carved.common.item.TwistedToolItem;
import net.farzad.twisted_and_carved.common.register.TDTags;
import net.farzad.twisted_and_carved.common.util.interfaces.TwistedGlintInterface;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
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
        if (stack.isItemBarVisible() && stack.isOf(TCItems.TWISTED_GREATAXE)) {
            int i = x + 2;
            int j = y + 13;
            int halfPoint = (13 / 2);

            drawContext.fill(RenderPipelines.GUI, i, j, i + 13, j + 2, -16777216);
            if (stack.getItemBarStep() <= halfPoint) {
                drawContext.fill(RenderPipelines.GUI, i, j, i + stack.getItemBarStep(), j + 1, ColorHelper.fullAlpha(stack.getItemBarColor()));

            } else if (stack.getItemBarStep() > halfPoint) {
                drawContext.fill(RenderPipelines.GUI, i, j, i + 6, j + 1, ColorHelper.fullAlpha(stack.getItemBarColor()));
                drawContext.fill(RenderPipelines.GUI, i + 7, j, i + stack.getItemBarStep(), j + 1, ColorHelper.fullAlpha(stack.getItemBarColor()));
            }
        }
    }

    @Inject(method = "drawStackOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;pushMatrix()Lorg/joml/Matrix3x2fStack;", shift = At.Shift.AFTER))
    private void twisted_and_carved$drawStackGlint(TextRenderer textRenderer, ItemStack stack, int x, int y, String stackCountText, CallbackInfo ci) {
        twistedAndCarved$drawItemGlint(stack,x,y);
    }

    @Override
    public void twistedAndCarved$drawItemGlint(ItemStack item, int x, int y) {
        if (!item.isEmpty()) {
            DrawContext drawContext = (DrawContext) (Object) this;
            if (item.isIn(TDTags.Items.TWISTED_SPIRIT)) {
                drawContext.fillGradient(x,y,x + 16,y + 8,ColorHelper.withAlpha(2,16770653),ColorHelper.withAlpha(45,12811848));
                drawContext.fillGradient(x,y + 8,x +16, y + 16,ColorHelper.withAlpha(45,12811848),  ColorHelper.withAlpha(2,16770653));
            }
            if (item.isIn(TDTags.Items.TWISTED_TOOL) && TwistedToolItem.hasAura(item.getOrDefault(TCDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY))) {
                drawContext.fillGradient(x,y,x +16, y + 8,ColorHelper.withAlpha(2,16711693),  ColorHelper.withAlpha(45,12779591));
                drawContext.fillGradient(x,y + 8,x +16, y + 16,ColorHelper.withAlpha(45,12779591),  ColorHelper.withAlpha(2,16711693));
            }
            if (item.isIn(TDTags.Items.KARMIUM)) {
                drawContext.fillGradient(x,y,x +16, y + 8,ColorHelper.withAlpha(2,11578541),  ColorHelper.withAlpha(45,14869218));
                drawContext.fillGradient(x,y + 8,x +16, y + 16,ColorHelper.withAlpha(45,14869218),  ColorHelper.withAlpha(2,11578541));
            }
        }


    }

}
