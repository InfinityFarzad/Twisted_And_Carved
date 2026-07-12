package net.farzad.twisted_and_carved.common.networking;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record GreataxeSoundLoopS2CPayload(int entityID) implements CustomPacketPayload {

    public static final Identifier GREATAXE_SOUND_LOOP_PAYLOAD_ID = TwistedAndCarved.id("play_greataxe_sound");
    public static final Type<GreataxeSoundLoopS2CPayload> ID = new Type<>(GREATAXE_SOUND_LOOP_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, GreataxeSoundLoopS2CPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, GreataxeSoundLoopS2CPayload::entityID, GreataxeSoundLoopS2CPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
