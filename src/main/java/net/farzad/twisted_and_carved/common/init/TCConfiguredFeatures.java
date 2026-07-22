package net.farzad.twisted_and_carved.common.init;

import net.farzad.twisted_and_carved.common.TwistedAndCarved;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.UpwardsBranchingTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class TCConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> TWISTED_TREE_KEY = registerKey("trees_twisted_forest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TWISTED_TREE = registerKey("twisted_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_TWISTED_TREE = registerKey("large_twisted_tree");


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, TwistedAndCarved.id(name));
    }

    public static void configure(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        TreeConfiguration twisted_tree = new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(TCBlocks.TWISTED_LOG),
                new FancyTrunkPlacer(12,4,4),
                BlockStateProvider.simple(TCBlocks.TWISTED_LEAVES),
                new FancyFoliagePlacer(ConstantInt.of(5),ConstantInt.of(3),2),
                new TwoLayersFeatureSize(6,2,3)
        ).build();


        context.register(LARGE_TWISTED_TREE, new ConfiguredFeature<>(Feature.TREE, twisted_tree));
        context.register(TWISTED_TREE, new ConfiguredFeature<>(Feature.TREE, twisted_tree));
    }
}
