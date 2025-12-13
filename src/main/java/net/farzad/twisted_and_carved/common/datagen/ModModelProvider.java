package net.farzad.twisted_and_carved.common.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.farzad.twisted_and_carved.common.block.ModBlocks;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ModelIds;
import net.minecraft.client.data.Models;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.util.collection.Pool;

public class ModModelProvider extends FabricModelProvider {
    public static final BlockFamily TWISTED_FAMILY = new BlockFamily.Builder(ModBlocks.TWISTED_PLANKS)
            .fence(ModBlocks.TWISTED_FENCE)
            .fenceGate(ModBlocks.TWISTED_FENCE_GATE)
            .stairs(ModBlocks.TWISTED_STAIRS)
            .slab(ModBlocks.TWISTED_SLAB)
            .door(ModBlocks.TWISTED_DOOR)
            .trapdoor(ModBlocks.TWISTED_TRAPDOOR)
            .pressurePlate(ModBlocks.TWISTED_PRESSURE_PLATE)
            .button(ModBlocks.TWISTED_BUTTON)
            .build();

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.createLogTexturePool(ModBlocks.TWISTED_LOG).log(ModBlocks.TWISTED_LOG).wood(ModBlocks.TWISTED_WOOD);
        blockStateModelGenerator.createLogTexturePool(ModBlocks.STRIPPED_TWISTED_LOG).log(ModBlocks.STRIPPED_TWISTED_LOG).wood(ModBlocks.STRIPPED_TWISTED_WOOD);
        blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.TWISTED_PLANKS).family(TWISTED_FAMILY);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.TWISTED_LEAVES);
        blockStateModelGenerator.registerTintableCrossBlockState(ModBlocks.TWISTED_SAPLING, BlockStateModelGenerator.CrossType.NOT_TINTED);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.KARMIUM_BLOCK);
        blockStateModelGenerator.registerMultifaceBlockModel(ModBlocks.TWISTED_VINE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModBlocks.TWISTED_SAPLING.asItem(), Models.GENERATED);
        itemModelGenerator.register(ModBlocks.KARMIUM_CHAIN.asItem(),Models.GENERATED);
        itemModelGenerator.register(ModBlocks.TWISTED_VINE.asItem(),Models.GENERATED);
        itemModelGenerator.register(ModItems.KARMIUM_NUGGET,Models.GENERATED);
    }
}