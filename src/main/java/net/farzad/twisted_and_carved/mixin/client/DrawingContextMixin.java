package net.farzad.twisted_and_carved.mixin.client;

import net.farzad.twisted_and_carved.common.item.TwistedToolItem;
import net.farzad.twisted_and_carved.common.register.TCDataComponents;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.farzad.twisted_and_carved.common.register.TDTags;
import net.farzad.twisted_and_carved.common.util.interfaces.TwistedGlintInterface;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public class DrawingContextMixin implements TwistedGlintInterface {

    @Inject(method = "renderItemBar", at = @At("TAIL"))
    private void twistedAndCarved$drawItemBar(ItemStack stack, int x, int y, CallbackInfo ci) {
        GuiGraphics drawContext = (GuiGraphics) (Object) this;
        if (stack.isBarVisible() && stack.is(TCItems.TWISTED_GREATAXE)) {
            int i = x + 2;
            int j = y + 13;
            int halfPoint = (13 / 2);

            drawContext.fill(RenderPipelines.GUI, i, j, i + 13, j + 2, -16777216);
            if (stack.getBarWidth() <= halfPoint) {
                drawContext.fill(RenderPipelines.GUI, i, j, i + stack.getBarWidth(), j + 1, ARGB.opaque(stack.getBarColor()));

            } else if (stack.getBarWidth() > halfPoint) {
                drawContext.fill(RenderPipelines.GUI, i, j, i + 6, j + 1, ARGB.opaque(stack.getBarColor()));
                drawContext.fill(RenderPipelines.GUI, i + 7, j, i + stack.getBarWidth(), j + 1, ARGB.opaque(stack.getBarColor()));
            }
        }
    }

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;pushMatrix()Lorg/joml/Matrix3x2fStack;", shift = At.Shift.AFTER))
    private void twisted_and_carved$drawStackGlint(Font textRenderer, ItemStack stack, int x, int y, String stackCountText, CallbackInfo ci) {
        twistedAndCarved$drawItemGlint(stack,x,y);
    }

    @Override
    public void twistedAndCarved$drawItemGlint(ItemStack item, int x, int y) {
        if (!item.isEmpty()) {
            GuiGraphics drawContext = (GuiGraphics) (Object) this;
            if (item.is(TDTags.Items.TWISTED_SPIRIT)) {
                drawContext.fillGradient(x,y,x + 16,y + 8,ARGB.color(2,16770653),ARGB.color(45,12811848));
                drawContext.fillGradient(x,y + 8,x +16, y + 16,ARGB.color(45,12811848),  ARGB.color(2,16770653));
            }
            if (item.is(TDTags.Items.TWISTED_TOOL) && TwistedToolItem.hasAura(item.getOrDefault(TCDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY))) {
                drawContext.fillGradient(x,y,x +16, y + 8,ARGB.color(2,16711693),  ARGB.color(45,12779591));
                drawContext.fillGradient(x,y + 8,x +16, y + 16,ARGB.color(45,12779591),  ARGB.color(2,16711693));
            }
            if (item.is(TDTags.Items.KARMIUM)) {
                drawContext.fillGradient(x,y,x +16, y + 8,ARGB.color(2,11578541),  ARGB.color(45,14869218));
                drawContext.fillGradient(x,y + 8,x +16, y + 16,ARGB.color(45,14869218),  ARGB.color(2,11578541));
            }
        }


    }

}
