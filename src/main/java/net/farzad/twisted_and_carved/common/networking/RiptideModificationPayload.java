package net.farzad.twisted_and_carved.common.networking;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public record RiptideModificationPayload(int entityID, ItemStack stack) implements CustomPacketPayload {

    public static final Identifier TWISTED_RIPTIDE_S2C_SYNC_PACKET = TwistedAndCarved.id("twisted_riptide_s2c_sync_payload");
    public static final Type<RiptideModificationPayload> ID = new Type<>(TWISTED_RIPTIDE_S2C_SYNC_PACKET);
    public static final StreamCodec<RegistryFriendlyByteBuf, RiptideModificationPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, RiptideModificationPayload::entityID, ItemStack.STREAM_CODEC,RiptideModificationPayload::stack,RiptideModificationPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
}

