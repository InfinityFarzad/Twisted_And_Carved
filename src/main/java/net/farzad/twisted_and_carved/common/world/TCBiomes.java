package net.farzad.twisted_and_carved.common.world;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public class TCBiomes {
    public static final ResourceKey<Biome> TWISTED_FOREST = registerBiome("twisted_forest");

    private static ResourceKey<Biome> registerBiome(String id) {
        return ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(TwistedAndCarved.MOD_ID, id));
    }

    private static void registerMaterialRules() {

    }

    public static void init() {
    }

}
