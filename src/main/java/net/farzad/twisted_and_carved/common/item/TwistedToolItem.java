package net.farzad.twisted_and_carved.common.item;

import net.akws.chiseled_lib.common.interfaces.item.CustomEffectsItem;
import net.akws.chiseled_lib.common.interfaces.item.SweepingItem;
import net.farzad.twisted_and_carved.common.register.TCParticles;
import net.farzad.twisted_and_carved.common.register.TCDataComponents;
import net.farzad.twisted_and_carved.common.register.TDTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;

public class TwistedToolItem extends Item implements SweepingItem, CustomEffectsItem {

    public TwistedToolItem(Settings settings) {
        super(settings);
    }

    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        ItemStack stored = stack.getOrDefault(TCDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY);
        if (slot.getStack().get(TCDataComponents.TWISTED_SPIRIT) == null || !slot.canTakePartial(player)) {
            return false;
        }
        else {
            if (!player.getItemCooldownManager().isCoolingDown(stack) && stored.isEmpty() && !cursorStackReference.get().isEmpty() && clickType.equals(ClickType.LEFT) && hasAura(cursorStackReference.get()) && isValidType(cursorStackReference.get())) {
                stack.set(TCDataComponents.TWISTED_SPIRIT, cursorStackReference.get());
                cursorStackReference.set(ItemStack.EMPTY);
                onContentChanged(player);
                playSpiritCastingSound(player);
                player.getItemCooldownManager().set(stack, 10);
                return true;
            } else if (!player.getItemCooldownManager().isCoolingDown(stack) && !stored.isEmpty() && cursorStackReference.get().isEmpty() && clickType.equals(ClickType.RIGHT)) {
                cursorStackReference.set(stored);
                stack.set(TCDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY);
                onContentChanged(player);
                playSpiritUnCastingSound(player);
                player.getItemCooldownManager().set(stack, 10);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        ItemStack stored = stack.getOrDefault(TCDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY);
            if (slot.getStack().get(TCDataComponents.TWISTED_SPIRIT) == null) {
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

    private static void playSpiritCastingSound(Entity entity) {
        entity.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1.0F, 1.0F);
        entity.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
    }

    private static void playSpiritUnCastingSound(Entity entity) {
        entity.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1.0F, -2.0F);
        entity.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, -2.0F);
    }

    private void onContentChanged(PlayerEntity user) {
        ScreenHandler screenHandler = user.currentScreenHandler;
        if (screenHandler != null) {
            screenHandler.onContentChanged(user.getInventory());
        }

    }

    public boolean isValidType(ItemStack stack) {
        return stack.contains(TCDataComponents.TWISTED_SPIRIT_DATA);
    }

    public static boolean hasAura(ItemStack stack) {
        return stack.isIn(TDTags.Items.TWISTED_SPIRIT);
    }

    @Override
    public boolean canDoSweepingAttack(ItemStack itemStack, boolean b, boolean b1, boolean b2) {
        return b && !b1 && !b2;
    }

    @Override
    public ParticleEffect sweepParticles() {
        return TCParticles.TWISTED_SWEEP_ATTACK;
    }
}
