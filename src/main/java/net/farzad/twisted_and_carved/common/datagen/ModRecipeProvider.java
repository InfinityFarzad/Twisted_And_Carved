package net.farzad.twisted_and_carved.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.farzad.twisted_and_carved.common.register.TCBlocks;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
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

                createDoorRecipe(TCBlocks.TWISTED_DOOR, Ingredient.ofItem(TCBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(TCBlocks.TWISTED_PLANKS), conditionsFromItem(TCBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);;
                createButtonRecipe(TCBlocks.TWISTED_BUTTON,Ingredient.ofItem(TCBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(TCBlocks.TWISTED_PLANKS), conditionsFromItem(TCBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);
                createFenceRecipe(TCBlocks.TWISTED_FENCE,Ingredient.ofItem(TCBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(TCBlocks.TWISTED_PLANKS), conditionsFromItem(TCBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);
                createFenceGateRecipe(TCBlocks.TWISTED_FENCE_GATE,Ingredient.ofItem(TCBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(TCBlocks.TWISTED_PLANKS), conditionsFromItem(TCBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);
                createSlabRecipe(RecipeCategory.BUILDING_BLOCKS, TCBlocks.TWISTED_SLAB,Ingredient.ofItem(TCBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(TCBlocks.TWISTED_PLANKS), conditionsFromItem(TCBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);;
                createStairsRecipe(TCBlocks.TWISTED_STAIRS,Ingredient.ofItem(TCBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(TCBlocks.TWISTED_PLANKS), conditionsFromItem(TCBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);;
                createTrapdoorRecipe(TCBlocks.TWISTED_TRAPDOOR,Ingredient.ofItem(TCBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(TCBlocks.TWISTED_PLANKS), conditionsFromItem(TCBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);;
                createPressurePlateRecipe(RecipeCategory.BUILDING_BLOCKS, TCBlocks.TWISTED_PRESSURE_PLATE,Ingredient.ofItem(TCBlocks.TWISTED_PLANKS))
                        .criterion(hasItem(TCBlocks.TWISTED_PLANKS), conditionsFromItem(TCBlocks.TWISTED_PLANKS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.BUILDING_BLOCKS, TCBlocks.KARMIUM_RAILING,16)
                        .input('c', Ingredient.ofItem(TCItems.KARMIUM_INGOT))
                        .input('g', Ingredient.ofItem(TCItems.KARMIUM_NUGGET))
                        .pattern("gcg")
                        .pattern("gcg")
                        .pattern("gcg")
                        .criterion(hasItem(TCItems.KARMIUM_INGOT), conditionsFromItem(TCItems.KARMIUM_NUGGET))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, TCItems.KARMIUM_NUGGET,9)
                        .input(TCItems.KARMIUM_INGOT)
                        .criterion(hasItem(TCItems.KARMIUM_INGOT), conditionsFromItem(TCItems.KARMIUM_INGOT))
                        .offerTo(exporter);

                createShaped(RecipeCategory.MISC, TCBlocks.KARMIUM_BLOCK,1)
                        .input('g', TCItems.KARMIUM_INGOT)
                        .pattern("ggg")
                        .pattern("ggg")
                        .pattern("ggg")
                        .criterion(hasItem(TCItems.KARMIUM_INGOT), conditionsFromItem(TCItems.KARMIUM_INGOT))
                        .offerTo(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "TwistedAndCarvedRecipeProvider";
    }
}
