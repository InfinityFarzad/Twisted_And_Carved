package net.farzad.twisted_and_carved.common.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ModNetworking {

    public static void init() {
        PayloadTypeRegistry.playS2C().register(GreataxeSoundLoopS2CPayload.ID, GreataxeSoundLoopS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ScytheSoundLoopS2CPayload.ID, ScytheSoundLoopS2CPayload.CODEC);
    }
}
