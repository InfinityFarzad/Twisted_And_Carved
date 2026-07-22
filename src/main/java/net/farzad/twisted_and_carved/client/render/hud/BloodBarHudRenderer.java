package net.farzad.twisted_and_carved.client.render.hud;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.init.TCDataComponents;
import net.farzad.twisted_and_carved.common.init.TCItems;
import net.farzad.twisted_and_carved.common.util.TwistedWeaponUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import java.util.Objects;

public class BloodBarHudRenderer implements HudElement {

    private int currentVal;
    private int oldVal;
    private int opacity = 3;
    private int x_offset = 0;
    private int y_offset = 0;

    public void tick() {
        Minecraft client = Minecraft.getInstance();
        if (client != null && client.player != null) {
            currentVal = client.player.getMainHandItem().getOrDefault(TCDataComponents.BLOOD_CHARGE,0);

            if (!getStack(client.player).has(TCDataComponents.BLOOD_CHARGE)) {
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
                opacity = (int) (99 -(Math.sin(client.getDeltaTracker().getGameTimeDeltaPartialTick(true) * 4) * 5));
                y_offset = Mth.randomBetweenInclusive(client.player.getRandom(), -1,1);
                x_offset = Mth.randomBetweenInclusive(client.player.getRandom(), -1,1);
            } else {
                x_offset = 0;
                y_offset = 0;
            }
        }

    }

    private ItemStack getStack(Player player) {
        if (player.getOffhandItem().is(TCItems.TWISTED_FALCHION)) {
            return player.getOffhandItem();
        } else if (player.getMainHandItem().is(TCItems.TWISTED_FALCHION)) {
            return player.getMainHandItem();
        } else {
            return player.getMainHandItem();
        }

    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();

        assert client.player != null;
        ItemStack stack = getStack(client.player);

        if (stack.is(TCItems.TWISTED_FALCHION) && Objects.equals(TwistedWeaponUtil.getAbilityID(stack), "bleeding")) {
            float s = stack.getOrDefault(TCDataComponents.BLOOD_CHARGE, 0) / 100F;

            int xCord = client.getWindow().getGuiScaledWidth() / 2 + 120;
            int yCord = client.getWindow().getGuiScaledHeight() - 28;
            context.blitSprite(RenderPipelines.GUI_TEXTURED, TwistedAndCarved.id("blood_bar/blood_bar"), xCord, yCord, 64, 32);
            context.blitSprite(RenderPipelines.GUI_TEXTURED, TwistedAndCarved.id("blood_bar/blood_bar_slice"), xCord + 39 - (int) (s * 26), yCord + 14, (int) (s * 26), 4);
            context.blitSprite(RenderPipelines.GUI_TEXTURED, TwistedAndCarved.id("blood_bar/blood_bar_overlay"), xCord, yCord, 64, 32);

            context.text(client.font,"%" + stack.getOrDefault(TCDataComponents.BLOOD_CHARGE,0).toString(),client.getWindow().getGuiScaledWidth() / 2 + 130 + x_offset, client.getWindow().getGuiScaledHeight() - 16 + y_offset, ARGB.color(opacity* 255 / 20,16777215),true);
        }
    }
}
