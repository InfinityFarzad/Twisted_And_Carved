package net.farzad.twisted_and_carved.common.world;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class TCBiomes {
    public static final RegistryKey<Biome> TWISTED_FOREST = registerBiome("twisted_forest");

    private static RegistryKey<Biome> registerBiome(String id) {
        return RegistryKey.of(RegistryKeys.BIOME, Identifier.of(TwistedAndCarved.MOD_ID, id));
    }

    public static void init() {
    }
}
