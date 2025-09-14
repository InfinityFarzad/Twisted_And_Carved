package net.farzad.twisted_and_carved.common.networking;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record GreataxeSoundLoopS2CPayload(int entityID) implements CustomPayload {

    public static final Identifier GREATAXE_SOUND_LOOP_PAYLOAD_ID = Identifier.of(TwistedAndCarved.MOD_ID, "play_greataxe_sound");
    public static final Id<GreataxeSoundLoopS2CPayload> ID = new Id<>(GREATAXE_SOUND_LOOP_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, GreataxeSoundLoopS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, GreataxeSoundLoopS2CPayload::entityID, GreataxeSoundLoopS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
