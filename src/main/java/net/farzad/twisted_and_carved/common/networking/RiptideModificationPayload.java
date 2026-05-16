package net.farzad.twisted_and_carved.common.networking;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RiptideModificationPayload(int entityID, ItemStack stack) implements CustomPayload {

    public static final Identifier TWISTED_RIPTIDE_S2C_SYNC_PACKET = Identifier.of(TwistedAndCarved.MOD_ID, "twisted_riptide_s2c_sync_payload");
    public static final Id<RiptideModificationPayload> ID = new Id<>(TWISTED_RIPTIDE_S2C_SYNC_PACKET);
    public static final PacketCodec<RegistryByteBuf, RiptideModificationPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, RiptideModificationPayload::entityID, ItemStack.PACKET_CODEC,RiptideModificationPayload::stack,RiptideModificationPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
            return ID;
        }
}

