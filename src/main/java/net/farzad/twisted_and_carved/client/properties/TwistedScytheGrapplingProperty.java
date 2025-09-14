package net.farzad.twisted_and_carved.client.properties;

import com.mojang.serialization.MapCodec;
import net.farzad.twisted_and_carved.common.component.ModDataComponents;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TwistedScytheGrapplingProperty implements BooleanProperty {

    public static MapCodec<TwistedScytheGrapplingProperty> CODEC = MapCodec.unit(TwistedScytheGrapplingProperty::new);

    @Override
    public MapCodec<? extends BooleanProperty> getCodec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        return stack.getOrDefault(ModDataComponents.TWISTED_SCYTHE_GRAPPLING,false);
    }
}
