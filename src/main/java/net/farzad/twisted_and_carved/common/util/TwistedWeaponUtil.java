package net.farzad.twisted_and_carved.common.util;

import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.farzad.twisted_and_carved.common.init.TCDataComponents;
import net.minecraft.world.item.ItemStack;

public class TwistedWeaponUtil {

    public static String getAbilityID(ItemStack stack) {
        return stack.getOrDefault(TCDataComponents.TWISTED_SPIRIT,ItemStack.EMPTY).getOrDefault(TCDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).id();
    }

    public static String getAbilityTYPE(ItemStack stack) {
        return stack.getOrDefault(TCDataComponents.TWISTED_SPIRIT,ItemStack.EMPTY).getOrDefault(TCDataComponents.TWISTED_SPIRIT_DATA, TwistedSpiritComponent.EMPTY).type();
    }

}
