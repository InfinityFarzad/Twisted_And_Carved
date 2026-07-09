package net.farzad.twisted_and_carved.common.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerInventoryUtil {
    public static boolean hasEmptySlot(Player player, int slot) {
        Inventory playerInventory = player.getInventory();
        if (slot == -1) {
            return (playerInventory.getFreeSlot() != -1 && player.getOffhandItem().isEmpty()) || (playerInventory.getFreeSlot() != -1 && !player.getOffhandItem().isEmpty()) || (playerInventory.getFreeSlot() == -1 && player.getOffhandItem().isEmpty());
        } else {
            return playerInventory.getFreeSlot() != -1 || (playerInventory.getFreeSlot() == -1 && player.getOffhandItem().isEmpty());
        }

    }

    public static void returnToSlot(Player player, int slot, ItemStack stack) {
        Inventory playerInventory = player.getInventory();
        if (slot == -1) {
            if (player.getOffhandItem().isEmpty()) {
                if (player.level() instanceof ServerLevel) {
                    playerInventory.setItem(Inventory.SLOT_OFFHAND, stack);
                }
            } else {
                playerInventory.add(stack);
            }
        } else if (playerInventory.getItem(slot).isEmpty()) {
            playerInventory.setItem(slot, stack);
        } else if (!playerInventory.getItem(slot).isEmpty()) {
            if (playerInventory.getFreeSlot() != -1) {
                playerInventory.add(stack);
            } else {
                if (player.getOffhandItem().isEmpty()) {
                    playerInventory.setItem(Inventory.SLOT_OFFHAND,stack);
                }
            }

        }
    }
}
