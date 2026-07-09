package net.farzad.twisted_and_carved.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.farzad.twisted_and_carved.common.register.TCBlocks;
import net.farzad.twisted_and_carved.common.register.TCItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);

                doorBuilder(TCBlocks.TWISTED_DOOR, Ingredient.of(TCBlocks.TWISTED_PLANKS))
                        .unlockedBy(getHasName(TCBlocks.TWISTED_PLANKS), has(TCBlocks.TWISTED_PLANKS))
                        .save(output);;
                buttonBuilder(TCBlocks.TWISTED_BUTTON,Ingredient.of(TCBlocks.TWISTED_PLANKS))
                        .unlockedBy(getHasName(TCBlocks.TWISTED_PLANKS), has(TCBlocks.TWISTED_PLANKS))
                        .save(output);
                fenceBuilder(TCBlocks.TWISTED_FENCE,Ingredient.of(TCBlocks.TWISTED_PLANKS))
                        .unlockedBy(getHasName(TCBlocks.TWISTED_PLANKS), has(TCBlocks.TWISTED_PLANKS))
                        .save(output);
                fenceGateBuilder(TCBlocks.TWISTED_FENCE_GATE,Ingredient.of(TCBlocks.TWISTED_PLANKS))
                        .unlockedBy(getHasName(TCBlocks.TWISTED_PLANKS), has(TCBlocks.TWISTED_PLANKS))
                        .save(output);
                slabBuilder(RecipeCategory.BUILDING_BLOCKS, TCBlocks.TWISTED_SLAB,Ingredient.of(TCBlocks.TWISTED_PLANKS))
                        .unlockedBy(getHasName(TCBlocks.TWISTED_PLANKS), has(TCBlocks.TWISTED_PLANKS))
                        .save(output);;
                stairBuilder(TCBlocks.TWISTED_STAIRS,Ingredient.of(TCBlocks.TWISTED_PLANKS))
                        .unlockedBy(getHasName(TCBlocks.TWISTED_PLANKS), has(TCBlocks.TWISTED_PLANKS))
                        .save(output);;
                trapdoorBuilder(TCBlocks.TWISTED_TRAPDOOR,Ingredient.of(TCBlocks.TWISTED_PLANKS))
                        .unlockedBy(getHasName(TCBlocks.TWISTED_PLANKS), has(TCBlocks.TWISTED_PLANKS))
                        .save(output);;
                pressurePlateBuilder(RecipeCategory.BUILDING_BLOCKS, TCBlocks.TWISTED_PRESSURE_PLATE,Ingredient.of(TCBlocks.TWISTED_PLANKS))
                        .unlockedBy(getHasName(TCBlocks.TWISTED_PLANKS), has(TCBlocks.TWISTED_PLANKS))
                        .save(output);
                shaped(RecipeCategory.BUILDING_BLOCKS, TCBlocks.KARMIUM_RAILING,16)
                        .define('c', Ingredient.of(TCItems.KARMIUM_INGOT))
                        .define('g', Ingredient.of(TCItems.KARMIUM_NUGGET))
                        .pattern("gcg")
                        .pattern("gcg")
                        .pattern("gcg")
                        .unlockedBy(getHasName(TCItems.KARMIUM_INGOT), has(TCItems.KARMIUM_NUGGET))
                        .save(output);

                shapeless(RecipeCategory.MISC, TCItems.KARMIUM_NUGGET,9)
                        .requires(TCItems.KARMIUM_INGOT)
                        .unlockedBy(getHasName(TCItems.KARMIUM_INGOT), has(TCItems.KARMIUM_INGOT))
                        .save(output);

                shaped(RecipeCategory.MISC, TCBlocks.KARMIUM_BLOCK,1)
                        .define('g', TCItems.KARMIUM_INGOT)
                        .pattern("ggg")
                        .pattern("ggg")
                        .pattern("ggg")
                        .unlockedBy(getHasName(TCItems.KARMIUM_INGOT), has(TCItems.KARMIUM_INGOT))
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "TwistedAndCarvedRecipeProvider";
    }
}
