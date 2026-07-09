package net.farzad.twisted_and_carved.client.properties;

import com.mojang.serialization.MapCodec;
import net.farzad.twisted_and_carved.common.register.TCDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TwistedScytheGrapplingProperty implements ConditionalItemModelProperty {

    public static MapCodec<TwistedScytheGrapplingProperty> CODEC = MapCodec.unit(TwistedScytheGrapplingProperty::new);

    @Override
    public MapCodec<? extends ConditionalItemModelProperty> type() {
        return CODEC;
    }

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        return stack.getOrDefault(TCDataComponents.TWISTED_SCYTHE_GRAPPLING,false);
    }
}
