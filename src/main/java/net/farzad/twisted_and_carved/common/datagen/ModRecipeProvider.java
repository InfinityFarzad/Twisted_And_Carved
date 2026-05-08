package net.farzad.twisted_and_carved.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.farzad.twisted_and_carved.common.block.ModBlocks;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                RegistryWrapper.Impl<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);

                createDoorRecipe(ModBlocks.TWISTED_DOOR, Ingredient.ofItem(ModBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(ModBlocks.TWISTED_PLANKS), conditionsFromItem(ModBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);;
                createButtonRecipe(ModBlocks.TWISTED_BUTTON,Ingredient.ofItem(ModBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(ModBlocks.TWISTED_PLANKS), conditionsFromItem(ModBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);
                createFenceRecipe(ModBlocks.TWISTED_FENCE,Ingredient.ofItem(ModBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(ModBlocks.TWISTED_PLANKS), conditionsFromItem(ModBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);
                createFenceGateRecipe(ModBlocks.TWISTED_FENCE_GATE,Ingredient.ofItem(ModBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(ModBlocks.TWISTED_PLANKS), conditionsFromItem(ModBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);
                createSlabRecipe(RecipeCategory.BUILDING_BLOCKS,ModBlocks.TWISTED_SLAB,Ingredient.ofItem(ModBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(ModBlocks.TWISTED_PLANKS), conditionsFromItem(ModBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);;
                createStairsRecipe(ModBlocks.TWISTED_STAIRS,Ingredient.ofItem(ModBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(ModBlocks.TWISTED_PLANKS), conditionsFromItem(ModBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);;
                createTrapdoorRecipe(ModBlocks.TWISTED_TRAPDOOR,Ingredient.ofItem(ModBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(ModBlocks.TWISTED_PLANKS), conditionsFromItem(ModBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);;
                createPressurePlateRecipe(RecipeCategory.BUILDING_BLOCKS,ModBlocks.TWISTED_PRESSURE_PLATE,Ingredient.ofItem(ModBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(ModBlocks.TWISTED_PLANKS), conditionsFromItem(ModBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.BUILDING_BLOCKS,ModBlocks.KARMIUM_FENCE,16)
                        .input('c', Ingredient.ofItem(ModItems.KARMIUM_INGOT))
                        .input('g', Ingredient.ofItem(ModItems.KARMIUM_NUGGET))
                        .pattern("gcg")
                        .pattern("gcg")
                        .pattern("gcg")
                        .criterion(hasItem(ModItems.KARMIUM_INGOT), conditionsFromItem(ModItems.KARMIUM_NUGGET))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, ModItems.KARMIUM_NUGGET,9)
                        .input(ModItems.KARMIUM_INGOT)
                        .criterion(hasItem(ModItems.KARMIUM_INGOT), conditionsFromItem(ModItems.KARMIUM_INGOT))
                        .offerTo(exporter);

                createShaped(RecipeCategory.MISC, ModBlocks.KARMIUM_BLOCK,1)
                        .input('g',ModItems.KARMIUM_INGOT)
                        .pattern("ggg")
                        .pattern("ggg")
                        .pattern("ggg")
                        .criterion(hasItem(ModItems.KARMIUM_INGOT), conditionsFromItem(ModItems.KARMIUM_INGOT))
                        .offerTo(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "TwistedAndCarvedRecipeProvider";
    }
}
