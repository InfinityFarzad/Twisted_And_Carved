package net.farzad.twisted_and_carved.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record TwistedSpiritComponent(String type, String id) {
    public static TwistedSpiritComponent EMPTY = new TwistedSpiritComponent("default","default_id");
    public static final Codec<TwistedSpiritComponent> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.STRING.fieldOf("type").forGetter(TwistedSpiritComponent::type),
                Codec.STRING.fieldOf("id").forGetter(TwistedSpiritComponent::id)
        ).apply(builder, TwistedSpiritComponent::new);
    });
    public static final StreamCodec<RegistryFriendlyByteBuf, TwistedSpiritComponent> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, TwistedSpiritComponent::type,
            ByteBufCodecs.STRING_UTF8, TwistedSpiritComponent::id,
            TwistedSpiritComponent::new
    );

}
