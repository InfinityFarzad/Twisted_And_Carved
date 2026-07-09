package net.farzad.twisted_and_carved.common.networking;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ScytheSoundLoopS2CPayload(int entityID) implements CustomPacketPayload {

    public static final Identifier SCYTHE_SOUND_LOOP_PAYLOAD_ID = Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, "play_scythe_sound");
    public static final Type<ScytheSoundLoopS2CPayload> ID = new Type<>(SCYTHE_SOUND_LOOP_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ScytheSoundLoopS2CPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, ScytheSoundLoopS2CPayload::entityID, ScytheSoundLoopS2CPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
