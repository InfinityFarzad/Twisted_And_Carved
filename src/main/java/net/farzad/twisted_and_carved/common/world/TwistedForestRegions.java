package net.farzad.twisted_and_carved.common.world;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import terrablender.api.ParameterUtils;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

public class TwistedForestRegions extends Region {
    public TwistedForestRegions(Identifier name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();
        new ParameterUtils.ParameterPointListBuilder()
                .temperature(ParameterUtils.Temperature.COOL)
                .humidity(ParameterUtils.Humidity.span(ParameterUtils.Humidity.DRY, ParameterUtils.Humidity.HUMID))
                .continentalness(ParameterUtils.Continentalness.span(ParameterUtils.Continentalness.COAST, ParameterUtils.Continentalness.OCEAN))
                .erosion(ParameterUtils.Erosion.EROSION_0, ParameterUtils.Erosion.EROSION_2)
                .depth(ParameterUtils.Depth.SURFACE, ParameterUtils.Depth.FLOOR)
                .weirdness(ParameterUtils.Weirdness.MID_SLICE_NORMAL_ASCENDING, ParameterUtils.Weirdness.MID_SLICE_NORMAL_DESCENDING)
                .build().forEach(point -> builder.add(point, ModBiomes.TWISTED_FOREST));

        builder.build().forEach(mapper);
    }
}