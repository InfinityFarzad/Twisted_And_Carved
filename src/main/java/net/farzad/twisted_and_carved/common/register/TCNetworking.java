package net.farzad.twisted_and_carved.common.register;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.farzad.twisted_and_carved.common.networking.GreataxeSoundLoopS2CPayload;
import net.farzad.twisted_and_carved.common.networking.RiptideModificationPayload;
import net.farzad.twisted_and_carved.common.networking.ScytheSoundLoopS2CPayload;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class TCNetworking {

    public static void init() {
        PayloadTypeRegistry.playS2C().register(GreataxeSoundLoopS2CPayload.ID, GreataxeSoundLoopS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ScytheSoundLoopS2CPayload.ID, ScytheSoundLoopS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(RiptideModificationPayload.ID, RiptideModificationPayload.CODEC);

    }

    public static void sendPacketToAllClients(World world, CustomPayload payload) {
        if (!world.isClient() && world instanceof ServerWorld serverWorld) {
            for (ServerPlayerEntity player : PlayerLookup.world(serverWorld)) {
                ServerPlayNetworking.send(player,payload);
            }
        }
    }
}
