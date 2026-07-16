package net.farzad.twisted_and_carved.common.register;

import com.mojang.serialization.Codec;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class TCDataComponents {

    public static final DataComponentType<Integer> STRIDE_CHARGE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwistedAndCarved.id("stride_charge"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build()
    );

    public static final DataComponentType<Integer> BLOOD_CHARGE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwistedAndCarved.id("blood_charge"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build()
    );

    public static final DataComponentType<Boolean> TWISTED_SCYTHE_GRAPPLING = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwistedAndCarved.id("twisted_scythe_grappling"),
            DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build()
    );

    public static final DataComponentType<Integer> TWISTED_SCYTHE_UUID = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwistedAndCarved.id("twisted_scythe_uuid"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build()
    );

    public static final DataComponentType<TwistedSpiritComponent> TWISTED_SPIRIT_DATA  = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwistedAndCarved.id("twisted_spirit_data"),
            DataComponentType.<TwistedSpiritComponent>builder().persistent(TwistedSpiritComponent.CODEC).networkSynchronized(TwistedSpiritComponent.PACKET_CODEC).build()
    );

    public static final DataComponentType<ItemStack> TWISTED_SPIRIT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            TwistedAndCarved.id("twisted_spirit"),
            DataComponentType.<ItemStack>builder().persistent(ItemStack.CODEC).networkSynchronized(ItemStack.STREAM_CODEC).build()
    );



    public static void init() {
    }
}
