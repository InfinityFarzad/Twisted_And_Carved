package net.farzad.twisted_and_carved.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.farzad.twisted_and_carved.common.register.TCBlocks;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootSubProvider {
    public ModLootTableProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(TCBlocks.TWISTED_LOG);
        dropSelf(TCBlocks.TWISTED_WOOD);
        dropSelf(TCBlocks.STRIPPED_TWISTED_LOG);
        dropSelf(TCBlocks.STRIPPED_TWISTED_WOOD);
        dropSelf(TCBlocks.TWISTED_PLANKS);
        dropSelf(TCBlocks.TWISTED_BUTTON);
        dropSelf(TCBlocks.TWISTED_PRESSURE_PLATE);
        dropSelf(TCBlocks.TWISTED_STAIRS);
        dropSelf(TCBlocks.TWISTED_FENCE_GATE);
        dropSelf(TCBlocks.TWISTED_FENCE);
        add(TCBlocks.TWISTED_DOOR,createDoorTable(TCBlocks.TWISTED_DOOR));
        dropSelf(TCBlocks.TWISTED_TRAPDOOR);
        //addDropWithSilkTouch(ModBlocks.TWISTED_VINE);
        dropSelf(TCBlocks.KARMIUM_CHAIN);
        dropSelf(TCBlocks.TWISTED_COFFIN);
        add(TCBlocks.TWISTED_LEAVES, createLeavesDrops(TCBlocks.TWISTED_LEAVES, TCBlocks.TWISTED_SAPLING, 0.25f));
        add(TCBlocks.TWISTED_SLAB,createSlabItemTable(TCBlocks.TWISTED_SLAB));
        dropSelf(TCBlocks.TWISTED_SAPLING);
        dropSelf(TCBlocks.KARMIUM_BLOCK);
        dropPottedContents(TCBlocks.POTTED_TWISTED_SAPLING);

    }
}
