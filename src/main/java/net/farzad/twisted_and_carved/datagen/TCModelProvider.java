package net.farzad.twisted_and_carved.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.farzad.twisted_and_carved.common.init.TCBlocks;
import net.farzad.twisted_and_carved.common.init.TCItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.BlockFamily;

public class TCModelProvider extends FabricModelProvider {
    public static final BlockFamily TWISTED_FAMILY = new BlockFamily.Builder(TCBlocks.TWISTED_PLANKS)
            .fence(TCBlocks.TWISTED_FENCE)
            .fenceGate(TCBlocks.TWISTED_FENCE_GATE)
            .stairs(TCBlocks.TWISTED_STAIRS)
            .slab(TCBlocks.TWISTED_SLAB)
            .door(TCBlocks.TWISTED_DOOR)
            .trapdoor(TCBlocks.TWISTED_TRAPDOOR)
            .pressurePlate(TCBlocks.TWISTED_PRESSURE_PLATE)
            .button(TCBlocks.TWISTED_BUTTON)
            .getFamily();

    public TCModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.woodProvider(TCBlocks.TWISTED_LOG).logWithHorizontal(TCBlocks.TWISTED_LOG).wood(TCBlocks.TWISTED_WOOD);
        blockStateModelGenerator.woodProvider(TCBlocks.STRIPPED_TWISTED_LOG).logWithHorizontal(TCBlocks.STRIPPED_TWISTED_LOG).wood(TCBlocks.STRIPPED_TWISTED_WOOD);
        blockStateModelGenerator.family(TCBlocks.TWISTED_PLANKS).generateFor(TWISTED_FAMILY);
        blockStateModelGenerator.createTrivialCube(TCBlocks.TWISTED_LEAVES);
        blockStateModelGenerator.createCrossBlock(TCBlocks.TWISTED_SAPLING, BlockModelGenerators.PlantType.NOT_TINTED);
        blockStateModelGenerator.createTrivialCube(TCBlocks.KARMIUM_BLOCK);
        blockStateModelGenerator.createMultifaceBlockStates(TCBlocks.TWISTED_VINE);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(TCBlocks.TWISTED_SAPLING.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(TCBlocks.KARMIUM_CHAIN.asItem(),ModelTemplates.FLAT_ITEM);
        //itemModelGenerator.register(ModBlocks.TWISTED_VINE.asItem(),Models.GENERATED);
        itemModelGenerator.generateFlatItem(TCItems.KARMIUM_NUGGET,ModelTemplates.FLAT_ITEM);
    }
}