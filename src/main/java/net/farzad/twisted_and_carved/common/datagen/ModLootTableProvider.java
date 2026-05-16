package net.farzad.twisted_and_carved.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.farzad.twisted_and_carved.common.register.TCBlocks;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(TCBlocks.TWISTED_LOG);
        addDrop(TCBlocks.TWISTED_WOOD);
        addDrop(TCBlocks.STRIPPED_TWISTED_LOG);
        addDrop(TCBlocks.STRIPPED_TWISTED_WOOD);
        addDrop(TCBlocks.TWISTED_PLANKS);
        addDrop(TCBlocks.TWISTED_BUTTON);
        addDrop(TCBlocks.TWISTED_PRESSURE_PLATE);
        addDrop(TCBlocks.TWISTED_STAIRS);
        addDrop(TCBlocks.TWISTED_SLAB);
        addDrop(TCBlocks.TWISTED_FENCE_GATE);
        addDrop(TCBlocks.TWISTED_FENCE);
        addDrop(TCBlocks.TWISTED_DOOR,doorDrops(TCBlocks.TWISTED_DOOR));
        addDrop(TCBlocks.TWISTED_TRAPDOOR);
        //addDropWithSilkTouch(ModBlocks.TWISTED_VINE);
        addDrop(TCBlocks.KARMIUM_CHAIN);
        addDrop(TCBlocks.TWISTED_COFFIN);
        addDrop(TCBlocks.TWISTED_LEAVES, leavesDrops(TCBlocks.TWISTED_LEAVES, TCBlocks.TWISTED_SAPLING, 0.25f));
        addDrop(TCBlocks.TWISTED_SAPLING);
        addDrop(TCBlocks.KARMIUM_BLOCK);

    }
}
