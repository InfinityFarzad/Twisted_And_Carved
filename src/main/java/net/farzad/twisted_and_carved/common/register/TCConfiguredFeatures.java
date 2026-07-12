package net.farzad.twisted_and_carved.common.register;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class TCConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> TWISTED_TREE_KEY = registerKey("trees_twisted_forest");


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, TwistedAndCarved.id(name));
    }

    public static void init() {}
}
