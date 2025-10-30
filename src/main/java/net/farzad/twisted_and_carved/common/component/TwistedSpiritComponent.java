package net.farzad.twisted_and_carved.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record TwistedSpiritComponent(String type, String id) {
    public static TwistedSpiritComponent EMPTY = new TwistedSpiritComponent("default","default_id");
    public static final Codec<TwistedSpiritComponent> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.STRING.fieldOf("type").forGetter(TwistedSpiritComponent::type),
                Codec.STRING.fieldOf("id").forGetter(TwistedSpiritComponent::id)
        ).apply(builder, TwistedSpiritComponent::new);
    });
    public static final PacketCodec<RegistryByteBuf, TwistedSpiritComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, TwistedSpiritComponent::type,
            PacketCodecs.STRING, TwistedSpiritComponent::id,
            TwistedSpiritComponent::new
    );

}
