package net.farzad.twisted_and_carved.common.init;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.farzad.twisted_and_carved.common.networking.GreataxeSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.networking.RiptideModificationPayload;
import net.farzad.twisted_and_carved.common.networking.ScytheSoundLoopS2CPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class TCNetworking {

    public static void init() {
        PayloadTypeRegistry.clientboundPlay().register(GreataxeSoundLoopS2CPayload.ID, GreataxeSoundLoopS2CPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ScytheSoundLoopS2CPayload.ID, ScytheSoundLoopS2CPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(RiptideModificationPayload.ID, RiptideModificationPayload.CODEC);

    }

    public static void sendPacketToAllClients(Level world, CustomPacketPayload payload) {
        if (!world.isClientSide() && world instanceof ServerLevel serverWorld) {
            for (ServerPlayer player : PlayerLookup.level(serverWorld)) {
                ServerPlayNetworking.send(player,payload);
            }
        }
    }
}
