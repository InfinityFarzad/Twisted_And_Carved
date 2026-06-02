package net.farzad.twisted_and_carved.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public class PlayerInventoryUtil {
    public static boolean hasEmptySlot(PlayerEntity player, int slot) {
        PlayerInventory playerInventory = player.getInventory();
        if (slot == -1) {
            return (playerInventory.getEmptySlot() != -1 && player.getOffHandStack().isEmpty()) || (playerInventory.getEmptySlot() != -1 && !player.getOffHandStack().isEmpty()) || (playerInventory.getEmptySlot() == -1 && player.getOffHandStack().isEmpty());
        } else {
            return playerInventory.getEmptySlot() != -1 || (playerInventory.getEmptySlot() == -1 && player.getOffHandStack().isEmpty());
        }

    }

    public static void returnToSlot(PlayerEntity player, int slot, ItemStack stack) {
        PlayerInventory playerInventory = player.getInventory();
        if (slot == -1) {
            if (player.getOffHandStack().isEmpty()) {
                if (player.getEntityWorld() instanceof ServerWorld) {
                    playerInventory.setStack(PlayerInventory.OFF_HAND_SLOT, stack);
                }
            } else {
                playerInventory.insertStack(stack);
            }
        } else if (playerInventory.getStack(slot).isEmpty()) {
            playerInventory.setStack(slot, stack);
        } else if (!playerInventory.getStack(slot).isEmpty()) {
            if (playerInventory.getEmptySlot() != -1) {
                playerInventory.insertStack(stack);
            } else {
                if (player.getOffHandStack().isEmpty()) {
                    playerInventory.setStack(PlayerInventory.OFF_HAND_SLOT,stack);
                }
            }

        }
    }
}
