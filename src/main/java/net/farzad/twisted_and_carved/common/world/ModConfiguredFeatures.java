package net.farzad.twisted_and_carved.common.world;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.farzad.twisted_and_carved.common.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.CherryFoliagePlacer;
import net.minecraft.world.gen.foliage.DarkOakFoliagePlacer;
import net.minecraft.world.gen.foliage.MegaPineFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.treedecorator.AlterGroundTreeDecorator;
import net.minecraft.world.gen.treedecorator.PlaceOnGroundTreeDecorator;
import net.minecraft.world.gen.trunk.MegaJungleTrunkPlacer;

import java.util.List;
import java.util.OptionalInt;

public class ModConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> TWISTED_TREE_MEDIUM_KEY = registerKey("twisted_tree_medium");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TWISTED_TREE_BIG_KEY = registerKey("twisted_tree_big");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TWISTED_TREE_SMALL_KEY = registerKey("twisted_tree_small");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        PlaceOnGroundTreeDecorator placeOnGroundTreeDecorator = new PlaceOnGroundTreeDecorator(96, 6, 2, new WeightedBlockStateProvider(VegetationConfiguredFeatures.leafLitter(1, 4)));
        register(context, TWISTED_TREE_SMALL_KEY, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.TWISTED_LOG),
                new MegaJungleTrunkPlacer(13, 2, 18),

                BlockStateProvider.of(ModBlocks.TWISTED_LEAVES),
                new MegaPineFoliagePlacer(ConstantIntProvider.create(4), ConstantIntProvider.create(3), ConstantIntProvider.create(4)),

                new TwoLayersFeatureSize(8, 2, 2, OptionalInt.empty()))
                .decorators(List.of(placeOnGroundTreeDecorator))
                .build());

        register(context, TWISTED_TREE_BIG_KEY, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.TWISTED_LOG),
                new MegaJungleTrunkPlacer(25, 18, 24),

                BlockStateProvider.of(ModBlocks.TWISTED_LEAVES),
                new CherryFoliagePlacer(ConstantIntProvider.create(5), ConstantIntProvider.create(0), ConstantIntProvider.create(4), 0.25f, 0.25f, 0.25f, 0.75f),

                new TwoLayersFeatureSize(8, 2, 2, OptionalInt.empty())).decorators(List.of(placeOnGroundTreeDecorator)).build());

        register(context, TWISTED_TREE_MEDIUM_KEY, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.TWISTED_LOG),
                new MegaJungleTrunkPlacer(13, 12, 14),

                BlockStateProvider.of(ModBlocks.TWISTED_LEAVES),
                new DarkOakFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0)),

                new TwoLayersFeatureSize(4, 2, 2, OptionalInt.empty()))
                .decorators(List.of(placeOnGroundTreeDecorator))
                .build());
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(TwistedAndCarved.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}