package net.farzad.twisted_and_carved.common.item.custom;

import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.util.ModTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;

public class TwistedToolItem extends Item {

    public TwistedToolItem(Settings settings) {
        super(settings);
    }

    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        ItemStack stored = stack.getOrDefault(ModDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY);
        if (slot.getStack().get(ModDataComponents.TWISTED_SPIRIT) == null) {
            return false;
        }
        else {
            if (stored.isEmpty() && !cursorStackReference.get().isEmpty() && clickType.equals(ClickType.RIGHT) && hasAura(cursorStackReference.get())) {
                stack.set(ModDataComponents.TWISTED_SPIRIT, cursorStackReference.get());
                cursorStackReference.set(ItemStack.EMPTY);
                onContentChanged(player);
                return true;
            } else if (!stored.isEmpty() && clickType.equals(ClickType.LEFT)) {
                cursorStackReference.set(stored);
                stack.set(ModDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY);
                onContentChanged(player);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        ItemStack stored = stack.getOrDefault(ModDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY);
            if (slot.getStack().get(ModDataComponents.TWISTED_SPIRIT) == null) {
                return false;
            }
            else {
                if (stored.isEmpty() && clickType.equals(ClickType.RIGHT)) {
                    return true;
                } else if (!stored.isEmpty() && clickType.equals(ClickType.LEFT)) {
                    return true;
                } else {
                    return false;
                }
            }


    }

    private void onContentChanged(PlayerEntity user) {
        ScreenHandler screenHandler = user.currentScreenHandler;
        if (screenHandler != null) {
            screenHandler.onContentChanged(user.getInventory());
        }

    }

    public static boolean hasAura(ItemStack stack) {
        return stack.isIn(ModTags.Items.TWISTED_SPIRIT);
    }

}
