package net.farzad.twisted_and_carved.client.render.hud;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Vector2d;

import java.util.Objects;

public class BloodBarHudRenderer implements HudRenderCallback {

    private int currentVal;
    private int oldVal;
    private int opacity = 3;
    private int x_offset = 0;
    private int y_offset = 0;

    public void tick() {
        currentVal = MinecraftClient.getInstance().player != null ? MinecraftClient.getInstance().player.getMainHandStack().getOrDefault(ModDataComponents.BLOOD_CHARGE,0) : 0;
        if (MinecraftClient.getInstance().player != null && !getStack(MinecraftClient.getInstance().player).contains(ModDataComponents.BLOOD_CHARGE)) {
            this.opacity = 3;
            this.oldVal = 0;
            this.currentVal = 0;
            x_offset = 0;
            y_offset = 0;
        }
        if (currentVal != oldVal || currentVal == 100) {
            oldVal = currentVal;
            opacity = currentVal >= 100 ? 255 * 20 : 15;
        }
        if (opacity >= 3 && currentVal != 100) {
            opacity -= 1;
        }

        if (currentVal >= 100) {
            y_offset = MathHelper.nextBetween(MinecraftClient.getInstance().player.getRandom(), -1,1);
            x_offset = MathHelper.nextBetween(MinecraftClient.getInstance().player.getRandom(), -1,1);
        } else {
            x_offset = 0;
            y_offset = 0;
        }

    }

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        Matrix4f transformationMatrix = drawContext.getMatrices().peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        MinecraftClient client = MinecraftClient.getInstance();

        assert client.player != null;
        ItemStack stack = getStack(client.player);


        Identifier texture = Identifier.of(TwistedAndCarved.MOD_ID, "textures/gui/blood_bar_" + getBloodChargeOverlay(stack) + ".png");
        if (stack.isOf(ModItems.TWISTED_FALCHION) && Objects.equals(TwistedWeaponUtil.getAbilityID(stack), "bleeding")) {

            drawContext.drawTexture(RenderLayer::getGuiTextured, texture, client.getWindow().getScaledWidth() / 2 + 120, client.getWindow().getScaledHeight() - 28, 0, 0, 64, 32, 64, 32);
            drawContext.drawText(client.textRenderer,"%" + stack.getOrDefault(ModDataComponents.BLOOD_CHARGE,0).toString(),client.getWindow().getScaledWidth() / 2 + 130 + x_offset, client.getWindow().getScaledHeight() - 16 + y_offset, ColorHelper.withAlpha(this.opacity * 255 / 20,16777215),true);
        }
    }

    private ItemStack getStack(PlayerEntity player) {
        if (player.getOffHandStack().isOf(ModItems.TWISTED_FALCHION)) {
            return player.getOffHandStack();
        } else if (player.getMainHandStack().isOf(ModItems.TWISTED_FALCHION)) {
            return player.getMainHandStack();
        } else {
            return player.getMainHandStack();
        }

    }

    private int getBloodChargeOverlay(ItemStack stack) {
        int comp = stack.getOrDefault(ModDataComponents.BLOOD_CHARGE, 0);
        if (comp <= 25 && comp > 0) {
            return 0;
        } else if (comp <= 50 && comp > 25) {
            return 1;
        } else if (comp <= 75 && comp > 50) {
            return 2;
        } else if (comp <= 100 && comp > 75) {
            return 3;
        } else {
            return 0;
        }
    }
}
