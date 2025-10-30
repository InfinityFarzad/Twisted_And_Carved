package net.farzad.twisted_and_carved.common.util;

import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.minecraft.item.ItemStack;

public class TwistedWeaponUtil {

    public static String getAbilityID(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.TWISTED_SPIRIT,ItemStack.EMPTY).getOrDefault(ModDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).id();
    }

    public static String getAbilityTYPE(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.TWISTED_SPIRIT,ItemStack.EMPTY).getOrDefault(ModDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).type();
    }

}
