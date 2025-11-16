package net.farzad.twisted_and_carved.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.farzad.twisted_and_carved.common.block.ModBlocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {

        getOrCreateTagBuilder(BlockTags.PLANKS).add(ModBlocks.TWISTED_PLANKS);

        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.TWISTED_LOG)
                .add(ModBlocks.STRIPPED_TWISTED_LOG)
                .add(ModBlocks.STRIPPED_TWISTED_WOOD)
                .add(ModBlocks.TWISTED_WOOD);

        getOrCreateTagBuilder(BlockTags.CLIMBABLE).add(ModBlocks.TWISTED_VINE);

        getOrCreateTagBuilder(BlockTags.SAPLINGS)
                .add(ModBlocks.TWISTED_SAPLING);

        getOrCreateTagBuilder(BlockTags.LOGS).add(ModBlocks.TWISTED_LOG);

        getOrCreateTagBuilder(BlockTags.LEAVES).add(ModBlocks.TWISTED_LEAVES);

        getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS).add(ModBlocks.TWISTED_STAIRS);
        getOrCreateTagBuilder(BlockTags.WOODEN_SLABS).add(ModBlocks.TWISTED_SLAB);
        getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS).add(ModBlocks.TWISTED_BUTTON);
        getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.TWISTED_PRESSURE_PLATE);
        getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS).add(ModBlocks.TWISTED_TRAPDOOR);
        getOrCreateTagBuilder(BlockTags.WOODEN_DOORS).add(ModBlocks.TWISTED_DOOR);
        getOrCreateTagBuilder(BlockTags.FENCE_GATES).add(ModBlocks.TWISTED_FENCE_GATE);
        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(ModBlocks.TWISTED_FENCE_GATE);
        getOrCreateTagBuilder(BlockTags.WOODEN_FENCES).add(ModBlocks.TWISTED_FENCE);
        getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL).add(ModBlocks.KARMIUM_BLOCK);
        getOrCreateTagBuilder(BlockTags.INCORRECT_FOR_GOLD_TOOL).add(ModBlocks.KARMIUM_BLOCK);
        getOrCreateTagBuilder(BlockTags.INCORRECT_FOR_IRON_TOOL).add(ModBlocks.KARMIUM_BLOCK);
        getOrCreateTagBuilder(BlockTags.INCORRECT_FOR_WOODEN_TOOL).add(ModBlocks.KARMIUM_BLOCK);
        getOrCreateTagBuilder(BlockTags.INCORRECT_FOR_STONE_TOOL).add(ModBlocks.KARMIUM_BLOCK);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(ModBlocks.KARMIUM_BLOCK).add(ModBlocks.KARMIUM_CHAIN);
        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(ModBlocks.TWISTED_COFFIN);

    }
}