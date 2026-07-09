package net.farzad.twisted_and_carved.common.item;

import net.akws.chiseled_lib.common.interfaces.item.CustomAttackItem;
import net.akws.chiseled_lib.common.interfaces.item.CustomEffectsItem;
import net.farzad.twisted_and_carved.common.register.TCDataComponents;
import net.farzad.twisted_and_carved.common.register.TCParticles;
import net.farzad.twisted_and_carved.common.register.TDTags;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TwistedToolItem extends Item implements CustomEffectsItem, CustomAttackItem {

    public TwistedToolItem(Properties settings) {
        super(settings);
    }

    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference) {
        ItemStack stored = stack.getOrDefault(TCDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY);
        if (slot.getItem().get(TCDataComponents.TWISTED_SPIRIT) == null || !slot.allowModification(player)) {
            return false;
        }
        else {
            if (!player.getCooldowns().isOnCooldown(stack) && stored.isEmpty() && !cursorStackReference.get().isEmpty() && clickType.equals(ClickAction.PRIMARY) && hasAura(cursorStackReference.get()) && isValidType(cursorStackReference.get())) {
                stack.set(TCDataComponents.TWISTED_SPIRIT, cursorStackReference.get());
                cursorStackReference.set(ItemStack.EMPTY);
                onContentChanged(player);
                playSpiritCastingSound(player);
                player.getCooldowns().addCooldown(stack, 10);
                return true;
            } else if (!player.getCooldowns().isOnCooldown(stack) && !stored.isEmpty() && cursorStackReference.get().isEmpty() && clickType.equals(ClickAction.SECONDARY)) {
                cursorStackReference.set(stored);
                stack.set(TCDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY);
                onContentChanged(player);
                playSpiritUnCastingSound(player);
                player.getCooldowns().addCooldown(stack, 10);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickType, Player player) {
        ItemStack stored = stack.getOrDefault(TCDataComponents.TWISTED_SPIRIT, ItemStack.EMPTY);
            if (slot.getItem().get(TCDataComponents.TWISTED_SPIRIT) == null) {
                return false;
            }
            else {
                if (stored.isEmpty() && clickType.equals(ClickAction.SECONDARY)) {
                    return true;
                } else if (!stored.isEmpty() && clickType.equals(ClickAction.PRIMARY)) {
                    return true;
                } else {
                    return false;
                }
            }
    }

    private static void playSpiritCastingSound(Entity entity) {
        entity.playSound(SoundEvents.END_PORTAL_FRAME_FILL, 1.0F, 1.0F);
        entity.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
    }

    private static void playSpiritUnCastingSound(Entity entity) {
        entity.playSound(SoundEvents.END_PORTAL_FRAME_FILL, 1.0F, -2.0F);
        entity.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, -2.0F);
    }

    private void onContentChanged(Player user) {
        AbstractContainerMenu screenHandler = user.containerMenu;
        if (screenHandler != null) {
            screenHandler.slotsChanged(user.getInventory());
        }

    }

    public boolean isValidType(ItemStack stack) {
        return stack.has(TCDataComponents.TWISTED_SPIRIT_DATA);
    }

    public static boolean hasAura(ItemStack stack) {
        return stack.is(TDTags.Items.TWISTED_SPIRIT);
    }

    @Override
    public boolean canDoSweepingAttack(ItemStack itemStack, boolean b, boolean b1, boolean b2) {
        return b && !b1 && !b2;
    }

    @Override
    public ParticleOptions sweepParticles(ItemStack stack) {
        return TCParticles.TWISTED_SWEEP_ATTACK;
    }
}
