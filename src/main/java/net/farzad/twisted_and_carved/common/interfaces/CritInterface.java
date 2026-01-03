package net.farzad.twisted_and_carved.common.interfaces;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface CritInterface {
    void onCrit(LivingEntity attacker, LivingEntity target, ItemStack stack);
}
