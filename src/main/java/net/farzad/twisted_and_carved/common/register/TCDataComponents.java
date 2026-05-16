package net.farzad.twisted_and_carved.common.register;

import com.mojang.serialization.Codec;
import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.component.TwistedSpiritComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TCDataComponents {

    public static final ComponentType<Integer> STRIDE_CHARGE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(TwistedAndCarved.MOD_ID, "stride_charge"),
            ComponentType.<Integer>builder().codec(Codec.INT).packetCodec(PacketCodecs.INTEGER).build()
    );

    public static final ComponentType<Integer> BLOOD_CHARGE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(TwistedAndCarved.MOD_ID, "blood_charge"),
            ComponentType.<Integer>builder().codec(Codec.INT).packetCodec(PacketCodecs.INTEGER).build()
    );

    public static final ComponentType<Boolean> TWISTED_SCYTHE_GRAPPLING = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(TwistedAndCarved.MOD_ID,"twisted_scythe_grappling"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOLEAN).build()
    );

    public static final ComponentType<TwistedSpiritComponent> TWISTED_SPIRIT_DATA  = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(TwistedAndCarved.MOD_ID,"twisted_spirit_data"),
            ComponentType.<TwistedSpiritComponent>builder().codec(TwistedSpiritComponent.CODEC).packetCodec(TwistedSpiritComponent.PACKET_CODEC).build()
    );

    public static final ComponentType<ItemStack> TWISTED_SPIRIT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(TwistedAndCarved.MOD_ID,"twisted_spirit"),
            ComponentType.<ItemStack>builder().codec(ItemStack.CODEC).packetCodec(ItemStack.PACKET_CODEC).build()
    );



    public static void init() {
    }
}
