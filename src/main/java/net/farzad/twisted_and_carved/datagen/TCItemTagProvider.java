package net.farzad.twisted_and_carved.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.farzad.twisted_and_carved.common.init.TCBlocks;
import net.farzad.twisted_and_carved.common.init.TCItems;
import net.farzad.twisted_and_carved.common.init.TCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import java.util.concurrent.CompletableFuture;

public class TCItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public TCItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(ItemTags.AXES).add(TCItems.TWISTED_GREATAXE);
        valueLookupBuilder(ItemTags.HOES).add(TCItems.TWISTED_SCYTHE);
        valueLookupBuilder(ItemTags.SWORDS).add(TCItems.TWISTED_FALCHION).add(TCItems.TWISTED_GLAIVE);
        valueLookupBuilder(TCTags.Items.TWISTED_TOOL_REPAIR_INGREDIENT).add(TCItems.KARMIUM_INGOT);
        valueLookupBuilder(ItemTags.SHARP_WEAPON_ENCHANTABLE).add(TCItems.TWISTED_FALCHION).add(TCItems.TWISTED_GLAIVE);
        valueLookupBuilder(ItemTags.HOES).add(TCItems.TWISTED_SCYTHE);
        valueLookupBuilder(ItemTags.PLANKS).add(TCBlocks.TWISTED_PLANKS.asItem());
        valueLookupBuilder(ItemTags.SHARP_WEAPON_ENCHANTABLE).addOptionalTag(TCTags.Items.TWISTED_TOOL);
        valueLookupBuilder(ItemTags.LOGS_THAT_BURN)
                .add(TCBlocks.TWISTED_LOG.asItem())
                .add(TCBlocks.STRIPPED_TWISTED_LOG.asItem())
                .add(TCBlocks.STRIPPED_TWISTED_WOOD.asItem())
                .add(TCBlocks.TWISTED_WOOD.asItem());

        valueLookupBuilder(ItemTags.DIRT).add(TCBlocks.FESTERING_ROOTS.asItem());

        valueLookupBuilder(ItemTags.SAPLINGS)
                .add(TCBlocks.TWISTED_SAPLING.asItem());

        valueLookupBuilder(ItemTags.LEAVES).add(TCBlocks.TWISTED_LEAVES.asItem());
        valueLookupBuilder(TCTags.Items.KARMIUM)
                .add(TCItems.KARMIUM_INGOT)
                .add(TCItems.RAW_KARMIUM)
                .add(TCItems.KARMIUM_BLOCK);

        valueLookupBuilder(ItemTags.WOODEN_STAIRS).add(TCBlocks.TWISTED_STAIRS.asItem());
        valueLookupBuilder(ItemTags.WOODEN_SLABS).add(TCBlocks.TWISTED_SLAB.asItem());
        valueLookupBuilder(ItemTags.WOODEN_BUTTONS).add(TCBlocks.TWISTED_BUTTON.asItem());
        valueLookupBuilder(ItemTags.WOODEN_PRESSURE_PLATES).add(TCBlocks.TWISTED_PRESSURE_PLATE.asItem());
        valueLookupBuilder(ItemTags.WOODEN_TRAPDOORS).add(TCBlocks.TWISTED_TRAPDOOR.asItem());
        valueLookupBuilder(ItemTags.WOODEN_DOORS).add(TCBlocks.TWISTED_DOOR.asItem());
        valueLookupBuilder(ItemTags.FENCE_GATES).add(TCBlocks.TWISTED_FENCE_GATE.asItem());
        valueLookupBuilder(ItemTags.WOODEN_FENCES).add(TCBlocks.TWISTED_FENCE.asItem());
        valueLookupBuilder(TCTags.Items.TWISTED_SPIRIT).add(TCItems.BLEEDING_SPIRIT);
        valueLookupBuilder(TCTags.Items.TWISTED_SPIRIT).add(TCItems.HARVEST_SPIRIT);
        valueLookupBuilder(TCTags.Items.TWISTED_SPIRIT).add(TCItems.GRAPPLING_SPIRIT);
        valueLookupBuilder(TCTags.Items.TWISTED_SPIRIT).add(TCItems.STRIDE_SPIRIT);
        valueLookupBuilder(TCTags.Items.TWISTED_SPIRIT).add(TCItems.SWEEPING_SPIRIT);
        valueLookupBuilder(TCTags.Items.TWISTED_SPIRIT).add(TCItems.TOMAHAWK_SPIRIT);
    }
}