package net.farzad.twisted_and_carved.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.farzad.twisted_and_carved.common.block.ModBlocks;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.TWISTED_LOG);
        addDrop(ModBlocks.TWISTED_WOOD);
        addDrop(ModBlocks.STRIPPED_TWISTED_LOG);
        addDrop(ModBlocks.STRIPPED_TWISTED_WOOD);
        addDrop(ModBlocks.TWISTED_PLANKS);
        addDrop(ModBlocks.TWISTED_BUTTON);
        addDrop(ModBlocks.TWISTED_PRESSURE_PLATE);
        addDrop(ModBlocks.TWISTED_STAIRS);
        addDrop(ModBlocks.TWISTED_SLAB);
        addDrop(ModBlocks.TWISTED_FENCE_GATE);
        addDrop(ModBlocks.TWISTED_FENCE);
        addDrop(ModBlocks.TWISTED_DOOR);
        addDrop(ModBlocks.TWISTED_TRAPDOOR);
        addDropWithSilkTouch(ModBlocks.TWISTED_VINE);
        addDrop(ModBlocks.KARMIUM_CHAIN);
        addDrop(ModBlocks.TWISTED_COFFIN);
        addDrop(ModBlocks.TWISTED_LEAVES, leavesDrops(ModBlocks.TWISTED_LEAVES, ModBlocks.TWISTED_SAPLING, 0.25f));
        addDrop(ModBlocks.TWISTED_SAPLING);
        addDrop(ModBlocks.KARMIUM_BLOCK);
    }
}
