package net.farzad.twisted_and_carved.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface RiptideStackAccesor {
    @Accessor("riptideStack")
    ItemStack riptideStack();
}
