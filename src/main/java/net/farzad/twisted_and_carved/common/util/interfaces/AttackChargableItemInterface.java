package net.farzad.twisted_and_carved.common.util.interfaces;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface AttackChargableItemInterface {
    void onFullAttack(LivingEntity attacker, LivingEntity target, ItemStack stack);
}
