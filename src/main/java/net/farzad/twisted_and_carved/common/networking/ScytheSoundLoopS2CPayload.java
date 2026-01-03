package net.farzad.twisted_and_carved.common.networking;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ScytheSoundLoopS2CPayload(int entityID) implements CustomPayload {

    public static final Identifier SCYTHE_SOUND_LOOP_PAYLOAD_ID = Identifier.of(TwistedAndCarved.MOD_ID, "play_scythe_sound");
    public static final Id<ScytheSoundLoopS2CPayload> ID = new Id<>(SCYTHE_SOUND_LOOP_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, ScytheSoundLoopS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, ScytheSoundLoopS2CPayload::entityID, ScytheSoundLoopS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
