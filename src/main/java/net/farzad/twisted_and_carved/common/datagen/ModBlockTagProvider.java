package net.farzad.twisted_and_carved.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.farzad.twisted_and_carved.common.register.TCBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        this.valueLookupBuilder(BlockTags.PLANKS).add(TCBlocks.TWISTED_PLANKS);

        this.valueLookupBuilder(BlockTags.LOGS_THAT_BURN)
                .add(TCBlocks.TWISTED_LOG)
                .add(TCBlocks.STRIPPED_TWISTED_LOG)
                .add(TCBlocks.STRIPPED_TWISTED_WOOD)
                .add(TCBlocks.TWISTED_WOOD);

        valueLookupBuilder(BlockTags.CLIMBABLE).add(TCBlocks.TWISTED_VINE);
        valueLookupBuilder(BlockTags.FLOWER_POTS).add(TCBlocks.POTTED_TWISTED_SAPLING);
        valueLookupBuilder(BlockTags.SAPLINGS)
                .add(TCBlocks.TWISTED_SAPLING);

        valueLookupBuilder(BlockTags.LOGS).add(TCBlocks.TWISTED_LOG);
        valueLookupBuilder(BlockTags.VALID_SPAWN).add(TCBlocks.FESTERING_ROOTS);
        valueLookupBuilder(BlockTags.MUSHROOM_GROW_BLOCK).add(TCBlocks.FESTERING_ROOTS);
        valueLookupBuilder(BlockTags.BIG_DRIPLEAF_PLACEABLE).add(TCBlocks.FESTERING_ROOTS);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_SHOVEL).add(TCBlocks.FESTERING_ROOTS);
        valueLookupBuilder(BlockTags.SNIFFER_DIGGABLE_BLOCK).add(TCBlocks.FESTERING_ROOTS).add(TCBlocks.FESTERING_ROOTS);
        valueLookupBuilder(BlockTags.DIRT).add(TCBlocks.FESTERING_ROOTS);

        valueLookupBuilder(BlockTags.LEAVES).add(TCBlocks.TWISTED_LEAVES);

        valueLookupBuilder(BlockTags.WOODEN_STAIRS).add(TCBlocks.TWISTED_STAIRS);
        valueLookupBuilder(BlockTags.WOODEN_SLABS).add(TCBlocks.TWISTED_SLAB);
        valueLookupBuilder(BlockTags.WOODEN_BUTTONS).add(TCBlocks.TWISTED_BUTTON);
        valueLookupBuilder(BlockTags.WOODEN_PRESSURE_PLATES).add(TCBlocks.TWISTED_PRESSURE_PLATE);
        valueLookupBuilder(BlockTags.WOODEN_TRAPDOORS).add(TCBlocks.TWISTED_TRAPDOOR);
        valueLookupBuilder(BlockTags.WOODEN_DOORS).add(TCBlocks.TWISTED_DOOR);
        valueLookupBuilder(BlockTags.FENCE_GATES).add(TCBlocks.TWISTED_FENCE_GATE);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE).add(TCBlocks.TWISTED_FENCE_GATE);
        valueLookupBuilder(BlockTags.WOODEN_FENCES).add(TCBlocks.TWISTED_FENCE);
        valueLookupBuilder(BlockTags.NEEDS_DIAMOND_TOOL).add(TCBlocks.KARMIUM_BLOCK);
        valueLookupBuilder(BlockTags.INCORRECT_FOR_GOLD_TOOL).add(TCBlocks.KARMIUM_BLOCK);
        valueLookupBuilder(BlockTags.INCORRECT_FOR_IRON_TOOL).add(TCBlocks.KARMIUM_BLOCK);
        valueLookupBuilder(BlockTags.INCORRECT_FOR_WOODEN_TOOL).add(TCBlocks.KARMIUM_BLOCK);
        valueLookupBuilder(BlockTags.INCORRECT_FOR_STONE_TOOL).add(TCBlocks.KARMIUM_BLOCK);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(TCBlocks.KARMIUM_BLOCK).add(TCBlocks.KARMIUM_CHAIN).add(TCBlocks.KARMIUM_RAILING);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE).add(TCBlocks.TWISTED_COFFIN);

    }
}