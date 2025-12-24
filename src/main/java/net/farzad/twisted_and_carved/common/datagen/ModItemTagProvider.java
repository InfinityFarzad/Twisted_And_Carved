package net.farzad.twisted_and_carved.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.farzad.twisted_and_carved.common.block.ModBlocks;
import net.farzad.twisted_and_carved.common.item.ModItems;
import net.farzad.twisted_and_carved.common.util.ModTags;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ItemTags.AXES).add(ModItems.TWISTED_GREATAXE);
        getOrCreateTagBuilder(ItemTags.HOES).add(ModItems.TWISTED_SCYTHE);
        getOrCreateTagBuilder(ModTags.Items.TWISTED_TOOL_REPAIR_INGREDIENT).add(ModItems.KARMIUM_INGOT);
        getOrCreateTagBuilder(ItemTags.SWORD_ENCHANTABLE).add(ModItems.TWISTED_FALCHION).add(ModItems.TWISTED_GLAIVE);
        getOrCreateTagBuilder(ItemTags.HOES).add(ModItems.TWISTED_SCYTHE);
        getOrCreateTagBuilder(ItemTags.PLANKS).add(ModBlocks.TWISTED_PLANKS.asItem());
        getOrCreateTagBuilder(ItemTags.SHARP_WEAPON_ENCHANTABLE).addOptionalTag(ModTags.Items.TWISTED_TOOL);
        getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.TWISTED_LOG.asItem())
                .add(ModBlocks.STRIPPED_TWISTED_LOG.asItem())
                .add(ModBlocks.STRIPPED_TWISTED_WOOD.asItem())
                .add(ModBlocks.TWISTED_WOOD.asItem());

        getOrCreateTagBuilder(ItemTags.SAPLINGS)
                .add(ModBlocks.TWISTED_SAPLING.asItem());

        getOrCreateTagBuilder(ItemTags.LEAVES).add(ModBlocks.TWISTED_LEAVES.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS).add(ModBlocks.TWISTED_STAIRS.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_SLABS).add(ModBlocks.TWISTED_SLAB.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS).add(ModBlocks.TWISTED_BUTTON.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.TWISTED_PRESSURE_PLATE.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS).add(ModBlocks.TWISTED_TRAPDOOR.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_DOORS).add(ModBlocks.TWISTED_DOOR.asItem());
        getOrCreateTagBuilder(ItemTags.FENCE_GATES).add(ModBlocks.TWISTED_FENCE_GATE.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_FENCES).add(ModBlocks.TWISTED_FENCE.asItem());
        getOrCreateTagBuilder(ModTags.Items.TWISTED_SPIRIT).add(ModItems.BLEEDING_SPIRIT);
        getOrCreateTagBuilder(ModTags.Items.TWISTED_SPIRIT).add(ModItems.HARVEST_SPIRIT);
        getOrCreateTagBuilder(ModTags.Items.TWISTED_SPIRIT).add(ModItems.GRAPPLING_SPIRIT);
        getOrCreateTagBuilder(ModTags.Items.TWISTED_SPIRIT).add(ModItems.STRIDE_SPIRIT);
        getOrCreateTagBuilder(ModTags.Items.TWISTED_SPIRIT).add(ModItems.SWEEPING_SPIRIT);
        getOrCreateTagBuilder(ModTags.Items.TWISTED_SPIRIT).add(ModItems.TOMAHAWK_SPIRIT);
    }
}