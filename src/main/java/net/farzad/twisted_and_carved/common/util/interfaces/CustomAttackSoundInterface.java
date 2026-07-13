package net.farzad.twisted_and_carved.common.util.interfaces;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

public interface CustomAttackSoundInterface {
    default SoundEvent getExtraAttackSound(ItemStack stack) { return null;}
}
