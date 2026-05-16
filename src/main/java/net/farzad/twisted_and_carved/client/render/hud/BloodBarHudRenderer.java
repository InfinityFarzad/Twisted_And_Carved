package net.farzad.twisted_and_carved.client.render.hud;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.register.TCDataComponents;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;

public class BloodBarHudRenderer implements HudElement {

    private int currentVal;
    private int oldVal;
    private int opacity = 3;
    private int x_offset = 0;
    private int y_offset = 0;

    public void tick() {
        currentVal = MinecraftClient.getInstance().player != null ? MinecraftClient.getInstance().player.getMainHandStack().getOrDefault(TCDataComponents.BLOOD_CHARGE,0) : 0;
        if (MinecraftClient.getInstance().player != null && !getStack(MinecraftClient.getInstance().player).contains(TCDataComponents.BLOOD_CHARGE)) {
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

    private ItemStack getStack(PlayerEntity player) {
        if (player.getOffHandStack().isOf(TCItems.TWISTED_FALCHION)) {
            return player.getOffHandStack();
        } else if (player.getMainHandStack().isOf(TCItems.TWISTED_FALCHION)) {
            return player.getMainHandStack();
        } else {
            return player.getMainHandStack();
        }

    }

    private int getBloodChargeOverlay(ItemStack stack) {
        int comp = stack.getOrDefault(TCDataComponents.BLOOD_CHARGE, 0);
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

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();

        assert client.player != null;
        ItemStack stack = getStack(client.player);

        if (stack.isOf(TCItems.TWISTED_FALCHION) && Objects.equals(TwistedWeaponUtil.getAbilityID(stack), "bleeding")) {
            float s = stack.getOrDefault(TCDataComponents.BLOOD_CHARGE, 0) / 100F;

            int xCord = client.getWindow().getScaledWidth() / 2 + 120;
            int yCord = client.getWindow().getScaledHeight() - 28;
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TwistedAndCarved.id("blood_bar/blood_bar"), xCord, yCord, 64, 32);
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TwistedAndCarved.id("blood_bar/blood_bar_slice"), xCord + 39 - (int) (s * 26), yCord + 14, (int) (s * 26), 4);
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TwistedAndCarved.id("blood_bar/blood_bar_overlay"), xCord, yCord, 64, 32);

            context.drawText(client.textRenderer,"%" + stack.getOrDefault(TCDataComponents.BLOOD_CHARGE,0).toString(),client.getWindow().getScaledWidth() / 2 + 130 + x_offset, client.getWindow().getScaledHeight() - 16 + y_offset, ColorHelper.withAlpha(opacity* 255 / 20,16777215),true);
        }
    }
}
