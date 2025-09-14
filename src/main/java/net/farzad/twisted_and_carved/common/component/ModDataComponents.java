package net.farzad.twisted_and_carved.common.component;

import com.mojang.serialization.Codec;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModDataComponents {

    public static final ComponentType<Integer> STRIDE_CHARGE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(TwistedAndCarved.MOD_ID, "stride_charge"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final ComponentType<Integer> BLOOD_CHARGE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(TwistedAndCarved.MOD_ID, "blood_charge"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final ComponentType<Boolean> TWISTED_SCYTHE_GRAPPLING = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(TwistedAndCarved.MOD_ID,"twisted_scythe_grappling"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );

    public static void init() {
    }
}
