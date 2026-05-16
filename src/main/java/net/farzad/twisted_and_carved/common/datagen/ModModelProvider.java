package net.farzad.twisted_and_carved.common.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.farzad.twisted_and_carved.common.register.TCBlocks;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.data.family.BlockFamily;

public class ModModelProvider extends FabricModelProvider {
    public static final BlockFamily TWISTED_FAMILY = new BlockFamily.Builder(TCBlocks.TWISTED_PLANKS)
            .fence(TCBlocks.TWISTED_FENCE)
            .fenceGate(TCBlocks.TWISTED_FENCE_GATE)
            .stairs(TCBlocks.TWISTED_STAIRS)
            .slab(TCBlocks.TWISTED_SLAB)
            .door(TCBlocks.TWISTED_DOOR)
            .trapdoor(TCBlocks.TWISTED_TRAPDOOR)
            .pressurePlate(TCBlocks.TWISTED_PRESSURE_PLATE)
            .button(TCBlocks.TWISTED_BUTTON)
            .build();

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.createLogTexturePool(TCBlocks.TWISTED_LOG).log(TCBlocks.TWISTED_LOG).wood(TCBlocks.TWISTED_WOOD);
        blockStateModelGenerator.createLogTexturePool(TCBlocks.STRIPPED_TWISTED_LOG).log(TCBlocks.STRIPPED_TWISTED_LOG).wood(TCBlocks.STRIPPED_TWISTED_WOOD);
        blockStateModelGenerator.registerCubeAllModelTexturePool(TCBlocks.TWISTED_PLANKS).family(TWISTED_FAMILY);
        blockStateModelGenerator.registerSimpleCubeAll(TCBlocks.TWISTED_LEAVES);
        blockStateModelGenerator.registerTintableCrossBlockState(TCBlocks.TWISTED_SAPLING, BlockStateModelGenerator.CrossType.NOT_TINTED);
        blockStateModelGenerator.registerSimpleCubeAll(TCBlocks.KARMIUM_BLOCK);
        //blockStateModelGenerator.registerMultifaceBlockModel(ModBlocks.TWISTED_VINE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(TCBlocks.TWISTED_SAPLING.asItem(), Models.GENERATED);
        itemModelGenerator.register(TCBlocks.KARMIUM_CHAIN.asItem(),Models.GENERATED);
        //itemModelGenerator.register(ModBlocks.TWISTED_VINE.asItem(),Models.GENERATED);
        itemModelGenerator.register(TCItems.KARMIUM_NUGGET,Models.GENERATED);
    }
}